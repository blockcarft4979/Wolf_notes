package com.bck.screen

import kotlinx.serialization.Serializable

@Serializable
object NoteListScreenData

@Serializable
class NotePreviewScreenData(val id: String)

@Serializable
class NoteEditScreenData(val id: String? = null)

@Serializable
object NotesRecycleBinScreenData
@Serializable
object AboutScreenData