package com.plannerinair.android.notes.preview.logic

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

typealias ReducerResult = Pair<NotesPreviewFeature.State, Set<NotesPreviewFeature.Eff>>

object NotesPreviewFeature {
    fun initialState(): State = State(persistentListOf("first", "two", "three"))

    data class State(val notes: ImmutableList<String>)

    sealed class Msg {
        data class OnTextNoteChange(val text: String, val index: Int) : Msg()
    }

    sealed class Eff {
    }

    fun reducer(msg: Msg, state: State): ReducerResult = when (msg) {
        is Msg.OnTextNoteChange -> state.onTextChange(msg.text, msg.index) to emptySet()
        }

    private fun State.onTextChange(text: String, index: Int): State {
        return copy(notes =  notes.toMutableList().also { it[index] = text }.toImmutableList())
    }
}
