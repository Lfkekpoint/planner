package com.plannerinair.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.cache
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plannerinair.android.di.provideFeature
import com.plannerinair.android.toplevel.logic.TopLevelFeature
import com.plannerinair.android.toplevel.ui.TopLevelScreen
import com.plannerinair.android.ui.theme.MyApplicationTheme
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
                feature.accept(TopLevelFeature.Msg.OnBack)
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
    composable(asComposeState().value, ::accept)
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
