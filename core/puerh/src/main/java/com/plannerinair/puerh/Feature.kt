package com.plannerinair.puerh

interface Feature<Msg : Any, Model : Any, Eff : Any> : Cancelable {
    val currentState: Model
    fun command(msg: Msg)
    fun listenState(listener: (model: Model) -> Unit): Cancelable
    fun listenEffect(listener: (eff: Eff) -> Unit): Cancelable
}
