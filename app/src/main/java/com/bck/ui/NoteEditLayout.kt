package com.bck.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bck.data.NotesData
import com.bck.loadNoteById
import com.bck.saveNoteToJson
import com.wolf.bck.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditLayout(
    noteId: String?, onSaved: () -> Unit, onCancel: () -> Unit, onAddNewNotes: () -> Unit
) {

    val existingNote = noteId?.let { loadNoteById(it) }
    val titleState = remember { mutableStateOf(existingNote?.title ?: "") }
    val contentState = remember { mutableStateOf(existingNote?.content ?: "") }
    val dataState = titleState.value + contentState.value
    val context = LocalContext.current

    val isSave = titleState.value != (existingNote?.title
        ?: "") || contentState.value != (existingNote?.content ?: "")
    val isNotEmpty = titleState.value.isNotBlank() || contentState.value.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (noteId == null) stringResource(R.string.new_note_hint) else stringResource(
                            R.string.edit_hint
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isSave && isNotEmpty) {
                                val note = NotesData(
                                    id = noteId ?: "",
                                    title = titleState.value,
                                    content = contentState.value
                                )
                                val result = saveNoteToJson(note)
                                if (result) {
                                    onAddNewNotes()
                                } else {
                                    Toast.makeText(
                                        context, R.string.save_failed, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                onCancel()
                            }
                        }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = stringResource(R.string.back_hint)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isSave && isNotEmpty) {
                                val note = NotesData(
                                    id = noteId ?: "",
                                    title = titleState.value,
                                    content = contentState.value
                                )
                                val result = saveNoteToJson(note)
                                if (result) {
                                    onSaved()
                                } else {
                                    Toast.makeText(
                                        context, R.string.save_failed, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_save_24),
                            contentDescription = stringResource(R.string.save_hint)
                        )
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize() // 改成 fillMaxSize，确保撑满全屏
                .padding(innerPadding) // 处理 Scaffold 顶部的 TopAppBar 高度
                .imePadding() // 处理键盘弹起（必须在 verticalScroll 前面！）
                .verticalScroll(rememberScrollState()) // 开启滚动
                .padding(16.dp) // 把内容的内边距放到滚动内部，这样滑动时更自然
        ) {
            OutlinedTextField(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                label = { Text(stringResource(R.string.title_hint)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = contentState.value,
                onValueChange = { contentState.value = it },
                label = { Text(stringResource(R.string.content_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp) // 🌟 建议给正文框加个最小高度，方便多行输入
            )
        }

    }
}

