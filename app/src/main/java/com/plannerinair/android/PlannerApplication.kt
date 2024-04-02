package com.plannerinair.android

import android.app.Application
import com.github.terrakok.cicerone.Cicerone

class PlannerApplication: Application() {
    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        internal lateinit var INSTANCE: PlannerApplication
            private set
    }
}