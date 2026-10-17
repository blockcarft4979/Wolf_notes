package com.bck.data

import kotlinx.serialization.Serializable

@Serializable
data class NotesData(
    val id: String = "",
    val content: String? = "",
    val title: String? = "",
    val time: String? = ""
)
object Path{
    const val NOTES_PATH: String = "BCK/.Contents/"
    const val RECYCLE_BIN_PATH: String = "BCK/.Recycle_bin/"
    const val GITHUB_LINK: String = "https://github.com/blockcarft4979"
}