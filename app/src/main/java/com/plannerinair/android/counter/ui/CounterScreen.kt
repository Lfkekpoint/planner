package com.plannerinair.android.counter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.plannerinair.android.counter.logic.CounterFeature

@Composable
fun CounterScreen(
    counterState: CounterFeature.State,
    modifier: Modifier,
    listener: (CounterFeature.Msg) -> Unit
) {
    Column(modifier = Modifier.padding(Dp(16f))) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { listener(CounterFeature.Msg.OnCounterChange(1)) }) {
                Text("Increase")
            }
            Text(text = counterState.counter.toString())
            Button(onClick = {
                listener(CounterFeature.Msg.OnCounterChange(-1))
            }) {
                Text("Decrease")
            }
        }
        Spacer(modifier = Modifier.height(Dp(16f)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                listener(CounterFeature.Msg.OnRandomClick)
            }) {
                if (counterState.progress != null) {
                    CircularProgressIndicator(
                        progress = counterState.progress / 100f,
                        Modifier.size(Dp(16f)),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Random")
                }
            }
        }
    }
}
