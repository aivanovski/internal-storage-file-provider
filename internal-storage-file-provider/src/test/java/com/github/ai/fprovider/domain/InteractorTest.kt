package com.github.ai.fprovider.domain

import com.github.ai.fprovider.domain.usecases.GetDirectoryListUseCase
import com.github.ai.fprovider.domain.usecases.GetFileInfoUseCase
import com.github.ai.fprovider.domain.usecases.GetMimeTypeUseCase
import com.github.ai.fprovider.domain.usecases.GetPathUseCase
import com.github.ai.fprovider.entity.ParsedUri
import com.github.ai.fprovider.entity.Projection
import com.github.ai.fprovider.entity.QueryType
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.Table
import com.github.ai.fprovider.entity.exception.InvalidPathException
import com.github.ai.fprovider.entity.exception.InvalidQueryTypeException
import com.github.ai.fprovider.test.TestData
import com.github.ai.fprovider.test.TestData.AUTH_TOKEN
import com.github.ai.fprovider.test.mockUri
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.io.FileNotFoundException

class InteractorTest {

    private val uriParser: UriParser = mockk()
    private val projectionMapper: ProjectionMapper = mockk()
    private val mimeTypeUseCase: GetMimeTypeUseCase = mockk()
    private val fileInfoUseCase: GetFileInfoUseCase = mockk()
    private val directoryListUseCase: GetDirectoryListUseCase = mockk()
    private val pathUseCase: GetPathUseCase = mockk()
    private val interactor = Interactor(
        uriParser = uriParser,
        projectionMapper = projectionMapper,
        mimeTypeUseCase = mimeTypeUseCase,
        fileInfoUseCase = fileInfoUseCase,
        directoryListUseCase = directoryListUseCase,
        pathUseCase = pathUseCase
    )

    @Test
    fun `getMimeType should route to GetMimeTypeUseCase`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(ParsedUri(QueryType.FILE_INFO, PATH, AUTH_TOKEN))
        every { mimeTypeUseCase.getMimeType(PATH) }.returns(Result.Success(RESULT))

        // act
        val mimeType = interactor.getMimeType(uri)

        // assert
        verify { mimeTypeUseCase.getMimeType(PATH) }
        assertThat(mimeType).isEqualTo(Result.Success(RESULT))
    }

    @Test
    fun `getMimeType should return InvalidPathException`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(null)

        // act
        val mimeType = interactor.getMimeType(uri)

        // assert
        assertThat(mimeType.isFailure).isEqualTo(true)
        assertThat(mimeType.getExceptionOrThrow()).isInstanceOf(InvalidPathException::class.java)
    }

    @Test
    fun `getMimeType should return InvalidQueryTypeException`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(ParsedUri(QueryType.DIRECTORY_LIST, PATH, AUTH_TOKEN))

        // act
        val mimeType = interactor.getMimeType(uri)

        // assert
        assertThat(mimeType.isFailure).isEqualTo(true)
        assertThat(mimeType.getExceptionOrThrow()).isInstanceOf(InvalidQueryTypeException::class.java)
    }

    @Test
    fun `query should return route to GetFileInfoUseCase`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(ParsedUri(QueryType.FILE_INFO, PATH, AUTH_TOKEN))
        every { projectionMapper.getProjectionFromColumns(ALL_COLUMNS) }.returns(ALL_PROJECTIONS)
        every { fileInfoUseCase.getFileInto(PATH, ALL_PROJECTIONS) }.returns(
            Result.Success(
                FILE_INFO_TABLE
            )
        )

        // act
        val result = interactor.query(uri, ALL_COLUMNS)

        // assert
        verify { fileInfoUseCase.getFileInto(PATH, ALL_PROJECTIONS) }
        assertThat(result).isEqualTo(Result.Success(FILE_INFO_TABLE))
    }

    @Test
    fun `query should route to GetDirectoryListUseCase`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(ParsedUri(QueryType.DIRECTORY_LIST, PATH, AUTH_TOKEN))
        every { projectionMapper.getProjectionFromColumns(ALL_COLUMNS) }.returns(ALL_PROJECTIONS)
        every { directoryListUseCase.getDirectoryList(PATH, ALL_PROJECTIONS) }.returns(
            Result.Success(
                DIRECTORY_LIST_TABLE
            )
        )

        // act
        val result = interactor.query(uri, ALL_COLUMNS)

        // assert
        assertThat(result).isEqualTo(Result.Success(DIRECTORY_LIST_TABLE))
    }

    @Test
    fun `query should return InvalidPathException`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(null)

        // act
        val result = interactor.query(uri, ALL_COLUMNS)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(InvalidPathException::class.java)
    }

    @Test
    fun `getRealPath should return real file path`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(ParsedUri(QueryType.FILE_INFO, PATH, AUTH_TOKEN))
        every { pathUseCase.getRealPath(PATH) }.returns(Result.Success(REAL_PATH))

        // act
        val result = interactor.getRealFilePath(uri)

        // assert
        assertThat(result).isEqualTo(Result.Success(REAL_PATH))
    }

    @Test
    fun `getRealPath should return InvalidPathException`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(null)

        // act
        val result = interactor.getRealFilePath(uri)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(InvalidPathException::class.java)
    }

    @Test
    fun `getRealPath should return InvalidQueryTypeException`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(ParsedUri(QueryType.DIRECTORY_LIST, PATH, AUTH_TOKEN))

        // act
        val result = interactor.getRealFilePath(uri)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(InvalidQueryTypeException::class.java)
    }

    @Test
    fun `getRealPath should return error from GetPathUseCase`() {
        // arrange
        val uri = mockUri(path = PATH)
        every { uriParser.parse(uri) }.returns(ParsedUri(QueryType.FILE_INFO, PATH, AUTH_TOKEN))
        every { pathUseCase.getRealPath(PATH) }.returns(Result.Failure(FileNotFoundException()))

        // act
        val result = interactor.getRealFilePath(uri)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(FileNotFoundException::class.java)
    }

    companion object {
        private val ALL_COLUMNS = Projection.values().map { it.columnName }
        private val ALL_PROJECTIONS = Projection.values().toList()
        private val FILE_INFO_TABLE = Table(
            rows = listOf(
                ALL_COLUMNS.map { "$it-placeholder-value" }
            ),
            columns = ALL_COLUMNS.map { "$it-placeholder" }
        )
        private val DIRECTORY_LIST_TABLE = Table(
            rows = listOf(),
            columns = ALL_COLUMNS.map { "$it" }
        )
        private const val PATH = "/path/placeholder"
        private const val RESULT = "result-placeholder"
        private const val REAL_PATH = "/data/data/path/placeholder"
    }
}