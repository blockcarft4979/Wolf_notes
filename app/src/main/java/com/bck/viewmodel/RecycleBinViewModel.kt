package com.bck.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bck.data.NotesData
import com.bck.data.Path
import com.bck.loadAllNotesFromDirectory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class RecycleBinViewModel : ViewModel() {
    private val _recycleBinData = MutableStateFlow<List<NotesData>>(emptyList())
    val recycleBinData: StateFlow<List<NotesData>> = _recycleBinData.asStateFlow()

    init { refresh() }

    private fun refresh() {
        _recycleBinData.value = loadAllNotesFromDirectory(Path.RECYCLE_BIN_DIR)
    }

    fun deleteNote(id: String) {
        val file = File(Path.RECYCLE_BIN_DIR, id)
        Log.d("RB", "delete ${file.absolutePath} exists=${file.exists()}")
        if (file.delete()) {
            _recycleBinData.value = loadAllNotesFromDirectory(Path.RECYCLE_BIN_DIR)
        }
    }

    fun recoverNote(id: String) {
        val src = File(Path.RECYCLE_BIN_DIR, id)
        val dst = File(Path.NOTES_DIR, id)
        if (src.exists()) {
            src.copyTo(dst, overwrite = true)
            src.delete()
            _recycleBinData.value = loadAllNotesFromDirectory(Path.RECYCLE_BIN_DIR)
        }
    }
}