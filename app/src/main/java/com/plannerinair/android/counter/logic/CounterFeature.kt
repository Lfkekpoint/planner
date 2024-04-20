package com.plannerinair.android.counter.logic

import com.plannerinair.android.counter.logic.CounterFeature.onTextChange
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

typealias ReducerResult = Pair<CounterFeature.State, Set<CounterFeature.Eff>>

object CounterFeature {
    fun initialState(): State = State(persistentListOf("one", "two", "three", "four", "five"))

    fun initialEffects(): Set<Eff> = setOf(Eff.GenerateRandomCounterChange)

    data class State(val notes: ImmutableList<String>)

    sealed class Msg {
        data class OnTextNoteChange(val text: String, val index: Int) : Msg()
    }

    sealed class Eff {
        object GenerateRandomCounterChange : Eff()
    }

    fun reducer(msg: Msg, state: State): ReducerResult = when (msg) {
        is Msg.OnTextNoteChange -> state.onTextChange(msg.text, msg.index) to emptySet()
        }

    private fun State.onTextChange(text: String, index: Int): State {
        return copy(notes =  notes.toMutableList().also { it[index] = text }.toImmutableList())
    }
}
