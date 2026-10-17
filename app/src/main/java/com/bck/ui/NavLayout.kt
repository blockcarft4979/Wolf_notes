package com.bck.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bck.screen.AboutScreenData
import com.bck.screen.NoteEditScreenData
import com.bck.screen.NoteListScreenData
import com.bck.screen.NotePreviewScreenData
import com.bck.screen.NotesRecycleBinScreenData
import com.bck.viewmodel.NotesListViewModel

@Composable
fun NavLayout() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val notesListViewModel: NotesListViewModel = viewModel()
    NavHost(
        navController, startDestination = NoteListScreenData, popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(350, delayMillis = 50))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        },
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350, delayMillis = 50))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            )
        }) {
        composable<NoteListScreenData> {
            NoteList(
                notesListViewModel,
                onToDelete = { note -> notesListViewModel.deleteNote(context, note) },
                onToEdit = {note->navController.navigate(NoteEditScreenData(note.id))},
                onClick = { notesData -> navController.navigate(NotePreviewScreenData(notesData.id)) },
                onAddNewNote = { navController.navigate(NoteEditScreenData(null)) },
                onToRecycleBin = { navController.navigate(NotesRecycleBinScreenData) },
                onToAbout = { navController.navigate(AboutScreenData) }
            )
        }
        composable<NotePreviewScreenData> { backStack ->
            val arg = backStack.toRoute<NotePreviewScreenData>()
            NotePreview(arg.id, onOpenEdit = {
                navController.navigate(
                    NoteEditScreenData(arg.id)
                )
            }, onBack = { navController.popBackStack() })
        }
        composable<NoteEditScreenData> { backStack ->
            val arg = backStack.toRoute<NoteEditScreenData>()
            NoteEditLayout(
                arg.id,
                onSaved = {
                    navController.popBackStack()
                    notesListViewModel.loadNotes()
                },
                onCancel = {
                    navController.popBackStack()
                    notesListViewModel.loadNotes()
                },
                onAddNewNotes = { navController.popBackStack() }
            )
        }
        composable<NotesRecycleBinScreenData> {
            NotesRecycleBin(
                onDelete = { }, onRecover = {}
            )
        }
        composable<AboutScreenData> { AboutLayout() }
    }
}

//class NavViewModel : ViewModel() {
//    var isReload by mutableStateOf(false)
//
//}