package com.plannerinair.android.notes.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.plannerinair.android.R
import com.plannerinair.android.notes.preview.logic.NotesPreviewFeature

@Composable
fun NotesPreviewScreen(
    modifier: Modifier,
    state: NotesPreviewFeature.State,
    listener: (NotesPreviewFeature.Msg) -> Unit
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(state.notes.size) { index ->
            NotePreviewItem(listener, index, state.notes[index])
        }
    }
}

@Composable
private fun NotePreviewItem(listener: (NotesPreviewFeature.Msg) -> Unit, index: Int, noteText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        Icon(painterResource(id = R.drawable.ic_featured_play_list), contentDescription = null)
        TextField(
            value = noteText,
            onValueChange = { listener(NotesPreviewFeature.Msg.OnTextNoteChange(it, index)) },
            modifier = Modifier.background(Color.White),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White
            )
        )
        Icon(painterResource(id = R.drawable.ic_arrow_left), contentDescription = null)
    }
}
