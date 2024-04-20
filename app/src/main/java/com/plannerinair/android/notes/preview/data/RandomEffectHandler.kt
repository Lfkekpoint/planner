package com.plannerinair.android.notes.preview.data

import com.plannerinair.android.notes.preview.logic.NotesPreviewFeature
import java.util.concurrent.ExecutorService
import kotlin.random.Random

fun ExecutorService.randomEffectInterpreter(
    eff: NotesPreviewFeature.Eff,
    listener: (NotesPreviewFeature.Msg) -> Unit
) = when (eff) {
    is NotesPreviewFeature.Eff.GenerateRandomCounterChange -> submit {
        for (i in 1..100) {
            Thread.sleep(10)
//            listener(CounterFeature.Msg.OnProgressPublish(i))
        }
        val value = Random.nextInt(100) - 50
//        listener(CounterFeature.Msg.OnCounterChange(value))
    }
}
