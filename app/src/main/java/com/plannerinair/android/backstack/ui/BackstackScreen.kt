package com.plannerinair.android.backstack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.plannerinair.android.backstack.logic.BackstackFeature

@Composable
fun BackstackScreen(
    state: BackstackFeature.State,
    listener: (BackstackFeature.Msg) -> Unit
) = Column(modifier = Modifier.padding(Dp(16f))) {
    Text(text = state.renderPath())
    BackstackCounter(
        "Current Screen",
        state.screen.counter,
        { listener(BackstackFeature.Msg.OnIncreaseClicked) },
        { listener(BackstackFeature.Msg.OnDecreaseClicked) }
    )
    state.previousScreen?.counter?.let {
        BackstackCounter(
            "Previous Screen",
            it,
            { listener(BackstackFeature.Msg.OnIncreasePreviousClicked) },
            { listener(BackstackFeature.Msg.OnDecreasePreviousClicked) }
        )
    }

    Text("Where to go next?")
    val (next1, next2) = state.canGoTo
    Button(onClick = { listener(BackstackFeature.Msg.OnGoToClicked(next1)) }) {
        Text(state.goButtonText(next1))
    }
    Button(onClick = { listener(BackstackFeature.Msg.OnGoToClicked(next2)) }) {
        Text(state.goButtonText(next2))
    }
}

fun BackstackFeature.State.renderPath(): String {
    val path = (backStack + screen).joinToString(separator = "/") { it.name }
    return "You are here: $path"
}

private fun BackstackFeature.State.goButtonText(screenName: String) =
    if (beenIn(screenName)) "Back to $screenName" else "Go to $screenName"


@Composable
private fun BackstackCounter(
    label: String,
    counter: Int,
    onIncClick: () -> Unit,
    onDecClick: () -> Unit
) = Column(modifier = Modifier.padding(Dp(8f))) {
    Text(label)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { onIncClick() }) {
            Text("Inc")
        }
        Text(counter.toString())
        Button(onClick = { onDecClick() }) {
            Text("Dec")
        }
    }
}
