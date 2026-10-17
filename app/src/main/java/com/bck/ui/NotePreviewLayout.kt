package com.bck.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bck.loadNoteById
import com.wolf.bck.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePreview(
    noteId: String,
    onOpenEdit: () -> Unit,
    onBack: () -> Unit
) {
    val existingNote = loadNoteById(noteId)
    val title = existingNote?.title?.takeIf { it.isNotBlank() }
    val content = existingNote?.content?.takeIf { it.isNotBlank() }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(R.drawable.baseline_arrow_back_24),
                            stringResource(R.string.back_hint)
                        )
                    }
                },
                title = { Text(stringResource(R.string.preview_note)) },
                actions = {
                    IconButton(onClick = onOpenEdit) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_edit_24),
                            contentDescription = stringResource(R.string.edit_hint)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(start = 15.dp, end = 15.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top

            ) {
                // 标题卡片
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(
                                Modifier,
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        } else {
                            Text(
                                text = "无标题",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (content != null) {
                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 24.sp
                            )
                        } else {
                            Text(
                                text = "无内容",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                // 可选：底部显示时间戳（如果存储了时间）
                existingNote?.time?.takeIf { it.isNotBlank() }?.let { timeStr ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = timeStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}