package com.github.ai.fprovider.demo.data

import android.content.Context
import android.database.Cursor
import com.github.ai.fprovider.Columns
import com.github.ai.fprovider.MimeTypes
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.data.entity.FilePath
import com.github.ai.fprovider.demo.data.entity.Result
import com.github.ai.fprovider.demo.data.exception.FailedToGetResponse
import com.github.ai.fprovider.demo.data.exception.FileNotFoundException
import com.github.ai.fprovider.demo.data.exception.FileSystemProviderException
import com.github.ai.fprovider.demo.extension.toUri

class FileSystemImpl(
    context: Context
) : FileSystem {

    private val contentResolver = context.contentResolver

    override fun getFile(path: FilePath): Result<FileEntity> {
        val uri = path.toUri()
        try {
            contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor == null) {
                    return Result.Failure(FailedToGetResponse())
                }

                val result = cursor.readFiles()
                return if (result.isSuccess) {
                    val files = result.getOrThrow()
                    if (files.isNotEmpty()) {
                        Result.Success(files.first())
                    } else {
                        Result.Failure(FileNotFoundException(uri))
                    }
                } else {
                    result.takeError()
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.Failure(FileSystemProviderException(cause = exception))
        }
    }

    override fun getFileList(path: FilePath): Result<List<FileEntity>> {
        val uri = path.toUri()

        try {
            contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor == null) {
                    return Result.Failure(FailedToGetResponse())
                }

                return cursor.readFiles()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.Failure(FileSystemProviderException(cause = exception))
        }
    }

    private fun Cursor.readFiles(): Result<List<FileEntity>> {
        if (this.count == 0) {
            return Result.Success(emptyList())
        }

        val columnUriIdx: Int
        val columnNameIdx: Int
        val columnSizeIdx: Int
        val columnMimeTypIdx: Int
        try {
            columnUriIdx = getColumnIndexOrThrow(Columns.URI)
            columnNameIdx = getColumnIndexOrThrow(Columns.NAME)
            columnSizeIdx = getColumnIndexOrThrow(Columns.SIZE)
            columnMimeTypIdx = getColumnIndexOrThrow(Columns.MIME_TYPE)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            return Result.Failure(FileSystemProviderException(cause = exception))
        }

        val files = mutableListOf<FileEntity>()
        while (moveToNext()) {
            val uri = getString(columnUriIdx)
            val name = getString(columnNameIdx)
            val size = getString(columnSizeIdx)
            val mimeType = getString(columnMimeTypIdx)

            if (!uri.isNullOrBlank() &&
                !name.isNullOrBlank() &&
                SIZE_PATTERN.matcher(size).matches() &&
                mimeType != null
            ) {
                files.add(
                    FileEntity(
                        path = uri,
                        name = name,
                        size = size.toLong(),
                        isDirectory = (mimeType == MimeTypes.DIRECTORY)
                    )
                )
            }
        }

        return Result.Success(files)
    }

    companion object {
        private val SIZE_PATTERN = "^-?[0-9]+$".toPattern()
    }
}