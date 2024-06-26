package com.plannerinair.puerh

import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
typealias ExecutorEffectsInterpreter<Eff, Msg> = ExecutorService.(eff: Eff, listener: (Msg) -> Unit) -> Unit

class ExecutorEffectHandler<Msg : Any, Eff : Any>(
    private val effectsInterpreter: ExecutorEffectsInterpreter<Eff, Msg>,
    private val callerThreadExecutor: Executor,
    private val effectsExecutorService: ExecutorService
) : EffectHandler<Eff, Msg> {
    private var listener: ((Msg) -> Unit)? = null

    override fun setListener(listener: (Msg) -> Unit) {
        this.listener = { msg -> callerThreadExecutor.execute { listener(msg) } }
    }

    override fun handleEffect(eff: Eff) {
        effectsExecutorService.run {
            effectsInterpreter(eff, listener ?: {})
        }
    }

    override fun cancel() {
        effectsExecutorService.shutdownNow()
    }
}