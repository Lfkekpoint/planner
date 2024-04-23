package com.plannerinair.android.toplevel.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.plannerinair.android.R
import com.plannerinair.android.notes.preview.ui.NotesPreviewScreen
import com.plannerinair.android.toplevel.logic.TopLevelFeature
import com.plannerinair.android.toplevel.logic.TopLevelFeature.State.ScreenState

private val bottomItems = listOf(
    "Counter" to R.drawable.ic_baseline_counter_24,
    "Backstack" to R.drawable.ic_baseline_backstack_24,
    "Translate" to R.drawable.ic_baseline_translate_24
)

@Composable
fun TopLevelScreen(
    state: TopLevelFeature.State,
    listener: (TopLevelFeature.Msg) -> Unit
) = Scaffold(

    bottomBar = {
        NavigationBar  {
            val selectedIndex = state.currentScreenPos
            bottomItems.forEachIndexed { index, (title, icon) ->
                NavigationBarItem (
                    icon = { Icon(painterResource(icon), contentDescription = null) },
                    label = { Text(title) },
                    selected = selectedIndex == index,
                    onClick = {
                        when (index) {
                            0 -> listener(TopLevelFeature.Msg.OnNotesScreenSwitch)
                        }
                    }
                )
            }
        }
    }
) { innerPadding  ->
     when (val screen = state.currentScreen) {
        is ScreenState.Notes -> {
            NotesPreviewScreen(
                modifier = Modifier.padding(innerPadding),
                state = screen.state
            ) { listener(TopLevelFeature.Msg.Notes(it)) }
        }
    }
}
