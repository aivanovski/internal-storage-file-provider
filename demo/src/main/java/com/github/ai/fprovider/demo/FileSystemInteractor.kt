package com.github.ai.fprovider.demo

import android.content.Context
import com.github.ai.fprovider.demo.model.AssetFileNode
import com.github.ai.fprovider.demo.model.DirectoryNode
import com.github.ai.fprovider.demo.model.FileNode
import com.github.ai.fprovider.demo.model.StubFileNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtils
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileSystemInteractor(private val context: Context) {

    suspend fun createFiles() {
        withContext(Dispatchers.IO) {
            val dataRootDir = context.filesDir

            val dir = File(dataRootDir, FILE_TREE_ROOT.path)
            if (dir.exists()) {
                dir.deleteRecursively()
            }
            dir.mkdirs()

            createDirectory(FILE_TREE_ROOT, dir)
        }
    }

    private fun createDirectory(
        fileNode: FileNode,
        parentDir: File
    ) {
        val files = fileNode.files ?: return

        for (file in files) {
            when (file) {
                is DirectoryNode -> {
                    val dir = File(parentDir, file.name)
                    dir.mkdirs()

                    createDirectory(file, dir)
                }
                is AssetFileNode -> {
                    createFileFromAsset(parentDir, file)
                }
                is StubFileNode -> {
                    createFileFromStub(parentDir, file)
                }
            }
        }
    }

    private fun createFileFromStub(parent: File, file: StubFileNode) {
        val content = ByteArrayInputStream(file.content.toByteArray())
        writeContentToFile(content, File(parent, file.name))
    }

    private fun createFileFromAsset(parent: File, file: AssetFileNode) {
        val content = context.assets.open(file.path)
        writeContentToFile(content, File(parent, file.name))
    }

    private fun writeContentToFile(content: InputStream, destination: File) {
        val out = BufferedOutputStream(FileOutputStream(destination))
        IOUtils.copy(content, out)
        out.flush()
        out.close()
        content.close()
    }

    companion object {

        private val STUB_FILE_CONTENT = """
            filetype on
            syntax on
            filetype plugin indent on
        """.trimIndent()

        private val FILE_TREE_ROOT = DirectoryNode(
            path = "home",
            files = listOf(
                DirectoryNode(
                    path = "home/Documents",
                    files = listOf(
                        AssetFileNode(path = "home/Documents/vim.txt"),
                        AssetFileNode(path = "home/Documents/vim-cheatsheet.pdf")
                    )
                ),
                DirectoryNode(
                    path = "home/.vim",
                    files = listOf(
                        StubFileNode(
                            path = "home/.vim/vimrc",
                            content = STUB_FILE_CONTENT
                        )
                    )
                ),
                AssetFileNode(path = "home/exiting-vim.jpg")
            )
        )
    }
}