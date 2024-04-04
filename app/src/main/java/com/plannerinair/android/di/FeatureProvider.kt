package com.plannerinair.android.di

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.plannerinair.android.backstack.logic.BackstackFeature
import com.plannerinair.android.backstack.logic.SCREEN_NAMES
import com.plannerinair.android.counter.data.randomEffectInterpreter
import com.plannerinair.android.toplevel.logic.TopLevelFeature
import com.plannerinair.android.translate.data.TranslationApiEffectHandler
import com.plannerinair.puerh.ExecutorEffectHandler
import com.plannerinair.puerh.ExecutorEffectsInterpreter
import com.plannerinair.puerh.Feature
import com.plannerinair.puerh.SyncFeature
import com.plannerinair.puerh.wrapWithEffectHandler
import com.plannerinair.puerh.adapt
import kotlinx.serialization.json.Json
import java.util.concurrent.Executor
import java.util.concurrent.Executors
typealias NavGraph = Map<String, Pair<String, String>>

fun provideFeature(applicationContext: Context): Feature<TopLevelFeature.Msg, TopLevelFeature.State, TopLevelFeature.Eff> {
    val androidMainThreadExecutor = object : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }
    val ioPool = Executors.newCachedThreadPool()

    return SyncFeature(
        TopLevelFeature.initialState(
            backstackFeatureState = BackstackFeature.initialState(
                SCREEN_NAMES.first(),
                generateScreenGraph()
            )
        ),
        TopLevelFeature::reducer
    ).wrapWithEffectHandler(
        ExecutorEffectHandler(
            adaptedRandomEffectInterpreter,
            androidMainThreadExecutor,
            ioPool
        )
    ).wrapWithEffectHandler<TopLevelFeature.Msg, TopLevelFeature.State, TopLevelFeature.Eff>(
        TranslationApiEffectHandler(
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = false
            },
            androidMainThreadExecutor,
            ioPool
        ).adapt(
            effAdapter = { (it as? TopLevelFeature.Eff.TranslateEff)?.eff },
            msgAdapter = { TopLevelFeature.Msg.TranslateMsg(it) }
        )
    )
}

private val adaptedRandomEffectInterpreter: ExecutorEffectsInterpreter<TopLevelFeature.Eff, TopLevelFeature.Msg> =
    { eff, listener ->
        if (eff is TopLevelFeature.Eff.CounterEff) randomEffectInterpreter(eff.eff) {
            listener(TopLevelFeature.Msg.CounterMsg(it))
        }
    }

private fun generateScreenGraph(): NavGraph {
    val nameTriples = SCREEN_NAMES.shuffled().zipWithNext().zip(SCREEN_NAMES)
    return nameTriples.associate { (shuffled, name3) ->
        val (name1, name2) = shuffled
        name1 to (name2 to name3)
    }
}
