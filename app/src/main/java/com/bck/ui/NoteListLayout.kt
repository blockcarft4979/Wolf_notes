package com.bck.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bck.component.NoteCard
import com.bck.data.NotesData
import com.bck.viewmodel.NotesListViewModel
import com.wolf.bck.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteList(
    notesListViewModel: NotesListViewModel,
    onToDelete: (NotesData) -> Unit,
    onToEdit:(NotesData)-> Unit,
    onClick: (NotesData) -> Unit,
    onAddNewNote: () -> Unit,
    onToRecycleBin: () -> Unit,
    onToAbout: () -> Unit
) {

    val notesData by notesListViewModel.notesData.collectAsState()
    val expanded = rememberSaveable() { mutableStateOf(false) }
    var isRefreshing by rememberSaveable() { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.notes)) }, actions = {
            IconButton(onClick = { expanded.value = true })
            {
                Icon(
                    painter = painterResource(R.drawable.baseline_more_vert_24),
                    stringResource(R.string.more_hint)
                )
            }
            DropdownMenu(
                // modifier = Modifier.padding(5.dp),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.recycle_bin)) },
                    onClick = {
                        onToRecycleBin()
                        expanded.value = false
                    })
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.about)) },
                    onClick = {
                        onToAbout()
                        expanded.value = false
                    }
                )
            }
        })
    }, floatingActionButton = {
        SmallFloatingActionButton(
            modifier = Modifier.size(56.dp), onClick = onAddNewNote
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = "Add"
            )
        }
    }) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.padding(innerPadding),
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                scope.launch {
                    delay(300)
                    notesListViewModel.loadNotes()
                    isRefreshing = false
                }
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notesData, key = { it.id }) { note ->
                    @Suppress("DEPRECATION")
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            when (dismissValue) {
                                SwipeToDismissBoxValue.EndToStart -> {
                                    onToDelete(note)
                                    true
                                }
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    onToEdit(note)
                                    false
                                }
                                else -> false
                            }
                        })

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            // 滑动时显示的背景：红色 + 删除图标
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                                    else -> Color.Transparent
                                }, label = "bg_color"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    tint = MaterialTheme.colorScheme.primary.copy(0.8f),
                                    painter = painterResource(R.drawable.baseline_delete_sweep_24),
                                    contentDescription = stringResource(R.string.delete_hint)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_edit_24), "编辑"
                                )
                            }

                        },
                        modifier = Modifier.animateItem(),
                        enableDismissFromStartToEnd = true,
                        enableDismissFromEndToStart = true
                    ) {
                        // 正常显示的卡片
                        NoteCard(
                            note = note, onClick = { onClick(note) })
                    }
                }
            }
        }
    }

}
