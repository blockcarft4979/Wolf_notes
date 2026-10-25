package com.bck.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bck.component.HintCard
import com.bck.component.NoteCard
import com.bck.viewmodel.RecycleBinViewModel
import com.wolf.bck.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesRecycleBin(onRecover: () -> Unit) {
    val viewModel: RecycleBinViewModel = viewModel()
    val notes by viewModel.recycleBinData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.recycle_bin)) }
            )
        }
    ) { innerPadding ->
        if (notes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
            ) {
                items(notes, key = { it.id }) { note ->

                    NoteCard(note){
                        TextButton(
                            onClick = {
                                viewModel.recoverNote(note.id)
                                onRecover()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_restore_24),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.recover))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = { viewModel.deleteNote(note.id) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_delete_sweep_24),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.delete_hint))
                        }
                    }
                }
            }
        } else {
            HintCard(R.drawable.baseline_delete_sweep_24, stringResource(R.string.tips_2))
        }
    }
}
