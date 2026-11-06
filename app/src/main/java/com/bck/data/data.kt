package com.bck.data

import android.os.Environment
import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
@Keep
data class NotesData(
    val id: String = "",
    val content: String? = "",
    val title: String? = "",
    val time: String? = ""
)
object Path {

    val BASE_DIR: File = Environment.getExternalStorageDirectory()  // 按加载函数实际用的前缀调整

    val NOTES_DIR: File get() = File(BASE_DIR, "BCK/.Contents")
    val RECYCLE_BIN_DIR: File get() = File(BASE_DIR, "BCK/.Recycle_bin")

    // 兼容旧调用点（loadAllNotesFromDirectory 还接收 String）
    @Deprecated("改用 RECYCLE_BIN_DIR")
    const val RECYCLE_BIN_PATH = "BCK/.Recycle_bin/"
    @Deprecated("改用 NOTES_DIR")
    const val NOTES_PATH = "BCK/.Contents/"

    const val GITHUB_LINK = "https://github.com/blockcarft4979"

    fun resolve(dir: File, name: String): File = File(dir, name)
}