package com.plannerinair.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.cache
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import com.plannerinair.android.di.provideFeature
import com.plannerinair.android.toplevel.logic.TopLevelFeature
import com.plannerinair.android.toplevel.ui.TopLevelScreen
import com.plannerinair.puerh.Feature

class MainActivity : ComponentActivity() {

    val feature by lazy { provideFeature(applicationContext) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                feature.Application { state, listener -> TopLevelScreen(state, listener) }
        }
        feature.listenEffect(this::handleEffect)
        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                feature.command(TopLevelFeature.Msg.OnBack)
            }
        })
    }

    private fun handleEffect(eff: TopLevelFeature.Eff) = when (eff) {
        TopLevelFeature.Eff.Finish -> onBackPressedDispatcher.onBackPressed()
        else -> Unit
    }
}

@Composable
fun <Msg: Any, Model: Any> Feature<Msg, Model, *>.Application(composable: @Composable (model: Model, msgSink: (Msg) -> Unit) -> Unit) {
    composable(asComposeState().value, ::command)
}

@Composable
fun <T : Any> Feature<*, T, *>.asComposeState(): State<T> {
    val state = currentComposer.cache(false) { mutableStateOf(currentState) }
    DisposableEffect(this) {
        val cancelable =  listenState { state.value = it }
        onDispose { cancelable.cancel() }
    }
    return state
}
