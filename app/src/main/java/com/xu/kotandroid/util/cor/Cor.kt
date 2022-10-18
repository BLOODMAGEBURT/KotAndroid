package com.xu.kotandroid.util.cor

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors

/**
 * @Author Xu
 * Date：2022/10/13 11:33
 * Description：
 */
class Cor {

    suspend fun test() {
        CoroutineName("")

        Executors.newSingleThreadExecutor().asCoroutineDispatcher().use {
            withContext(it) {
                delay(1000)
                writeFile("test.txt")
                println()
                writeFile("itIsMyLife.txt")
                5
            }
        }
    }

    private fun writeFile(fileName: String) {
        val currentDir = System.getProperty("user.dir")?.plus("\\out")
        val file = File(currentDir, fileName)
        file.writeText("呵呵呵哈哈哈")
    }

}