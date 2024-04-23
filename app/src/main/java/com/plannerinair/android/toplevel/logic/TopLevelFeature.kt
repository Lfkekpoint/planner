package com.plannerinair.android.toplevel.logic

import com.plannerinair.android.notes.preview.logic.NotesPreviewFeature
import com.plannerinair.android.toplevel.logic.TopLevelFeature.State.ScreenState.*

private typealias ReducerResult = Pair<TopLevelFeature.State, Set<TopLevelFeature.Eff>>

object TopLevelFeature {
    fun initialState(
    ): State = State(
        screens = listOf(
            Notes(NotesPreviewFeature.initialState()),
        ),
        currentScreenPos = 0
    )

    data class State(
        val screens: List<ScreenState>,
        val currentScreenPos: Int
    ) {
        val currentScreen = screens[currentScreenPos]
        fun <T : ScreenState> changeCurrentScreen(block: T.() -> T): State {
            @Suppress("UNCHECKED_CAST") val newScreen = (currentScreen as? T)?.block()
            val newList = if (newScreen != null)
                screens.toMutableList().also { mutableScreens ->
                    mutableScreens[currentScreenPos] = newScreen
                } else screens
            return copy(screens = newList)
        }

        sealed class ScreenState {
            data class Notes(
                val state: NotesPreviewFeature.State
            ) : ScreenState()
        }
    }

    sealed class Msg {
        data class Notes(val msg: NotesPreviewFeature.Msg) : Msg()
        data object OnNotesScreenSwitch : Msg()
        data object OnBack : Msg()
    }

    sealed class Eff {
        object Finish : Eff()
        data class CounterEff(val eff: NotesPreviewFeature.Eff) : Eff()
    }

    fun reducer(msg: Msg, state: State): ReducerResult = when (state.currentScreen) {
        is Notes -> when (msg) {
            is Msg.Notes -> {
                reduceCounter(state.currentScreen, msg.msg, state)
            }
            is Msg.OnBack -> {
                state to setOf(Eff.Finish)
            }
            else -> state to emptySet()
        }
    }

    private fun reduceCounter(
        currentScreen: Notes,
        msg: NotesPreviewFeature.Msg,
        state: State
    ): ReducerResult {
        val (newScreenState, effs) = NotesPreviewFeature.reducer(msg, currentScreen.state)
        val newEffs = effs.mapTo(HashSet(), Eff::CounterEff)
        return state.changeCurrentScreen<Notes> { copy(state = newScreenState) } to newEffs
    }
}
