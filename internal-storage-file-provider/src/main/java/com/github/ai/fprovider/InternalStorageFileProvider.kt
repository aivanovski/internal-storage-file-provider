package com.github.ai.fprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.os.RemoteException
import com.github.ai.fprovider.data.InternalFileSystem
import com.github.ai.fprovider.data.dao.TokenDaoImpl
import com.github.ai.fprovider.data.serialization.TokenAndPathDeserializer
import com.github.ai.fprovider.data.serialization.TokenAndPathSerializer
import com.github.ai.fprovider.domain.FileModelFormatter
import com.github.ai.fprovider.domain.Interactor
import com.github.ai.fprovider.domain.MetaDataReader
import com.github.ai.fprovider.domain.MimeTypeProvider
import com.github.ai.fprovider.domain.ProjectionMapper
import com.github.ai.fprovider.domain.UriParser
import com.github.ai.fprovider.domain.usecases.CheckAuthTokenUseCase
import com.github.ai.fprovider.domain.usecases.GetDirectoryListUseCase
import com.github.ai.fprovider.domain.usecases.GetFileInfoUseCase
import com.github.ai.fprovider.domain.usecases.GetMimeTypeUseCase
import com.github.ai.fprovider.domain.usecases.GetPathUseCase
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.logging.AndroidLogcatLogger
import com.github.ai.fprovider.utils.toCursor
import java.io.File

/**
 * Serves directories and files from internal data directory [android.content.Context.getDataDir]
 */
class InternalStorageFileProvider constructor() : ContentProvider() {

    private val isLoggingEnabled = false
    private val logger = AndroidLogcatLogger(InternalStorageFileProvider::class)
    private lateinit var interactor: Interactor

    internal constructor(interactor: Interactor) : this() {
        this.interactor = interactor
    }

    override fun onCreate(): Boolean {
        val context = this.context ?: return false

        val rootPath = context.filesDir?.parentFile?.path ?: return false
        val fileSystem = InternalFileSystem(rootPath)
        val mimeTypeProvider = MimeTypeProvider()
        val fileModelFormatter = FileModelFormatter()
        val authority = MetaDataReader().readAuthority(context)
        val tokenDao = TokenDaoImpl(
            context = context,
            serializer = TokenAndPathSerializer(),
            deserializer = TokenAndPathDeserializer()
        )

        interactor = Interactor(
            uriParser = UriParser(),
            projectionMapper = ProjectionMapper(),
            checkTokenUseCase = CheckAuthTokenUseCase(tokenDao),
            mimeTypeUseCase = GetMimeTypeUseCase(
                fileSystem = fileSystem,
                mimeTypeProvider = mimeTypeProvider
            ),
            fileInfoUseCase = GetFileInfoUseCase(
                fileSystem = fileSystem,
                mimeTypeProvider = mimeTypeProvider,
                fileModelFormatter = fileModelFormatter,
                authority = authority
            ),
            directoryListUseCase = GetDirectoryListUseCase(
                fileSystem = fileSystem,
                mimeTypeProvider = mimeTypeProvider,
                fileModelFormatter = fileModelFormatter,
                authority = authority
            ),
            pathUseCase = GetPathUseCase(
                fileSystem = fileSystem
            )
        )

        return true
    }

    override fun getType(uri: Uri): String {
        val result = interactor.getMimeType(uri)

        if (isLoggingEnabled) {
            logger.d("getType: uri=$uri, result=$result")
        }

        if (result.isFailure) {
            throwExceptionFromResult(result)
        }

        return result.getOrThrow()
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val columns = projection?.toList() ?: emptyList()
        val result = interactor.query(uri, columns)

        if (isLoggingEnabled) {
            logger.d("query: uri=$uri, columns=$columns, result=$result")
        }

        if (result.isFailure) {
            throwExceptionFromResult(result)
        }

        return result.getOrThrow().toCursor()
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        throw RuntimeException()
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw RuntimeException()
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw RuntimeException()
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val getPathResult = interactor.getRealFilePath(uri)
        if (getPathResult.isFailure) {
            throwExceptionFromResult(getPathResult)
        }

        val path = getPathResult.getOrThrow()

        return ParcelFileDescriptor.open(File(path), ParcelFileDescriptor.MODE_READ_ONLY)
    }

    private fun throwExceptionFromResult(result: Result<*>): Nothing {
        val message = result.getExceptionOrThrow().toString()
        throw RemoteException(message)
    }
}