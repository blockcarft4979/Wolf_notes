package com.bck.viewmodel

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bck.data.NotesData
import com.bck.data.Path
import com.bck.loadAllNotesFromDirectory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class NotesListViewModel : ViewModel() {
    private val _notesData = MutableStateFlow<List<NotesData>>(emptyList())
    val notesData: StateFlow<List<NotesData>> = _notesData.asStateFlow()
    init {
        _notesData.value = loadAllNotesFromDirectory(Path.NOTES_PATH)
    }

    fun loadNotes() {
        _notesData.value = loadAllNotesFromDirectory(Path.NOTES_PATH)
    }
    fun deleteNote(context: Context, note: NotesData) {
        viewModelScope.launch {
            val sourceFile = File(Environment.getExternalStorageDirectory(), Path.NOTES_PATH + note.id)
            val recycleDir = File(Environment.getExternalStorageDirectory(), Path.RECYCLE_BIN_PATH)

            if (!recycleDir.exists()) recycleDir.mkdirs()
            val destFile = File(recycleDir, note.id)

            val success = if (sourceFile.exists()) {
                if (destFile.exists()) destFile.delete()
                sourceFile.renameTo(destFile)
            } else {
                false
            }

            if (success) {
                _notesData.value = _notesData.value.filter { it.id != note.id }
                Toast.makeText(context, "已移至回收站", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "移动失败", Toast.LENGTH_SHORT).show()
            }
        }
    }


}