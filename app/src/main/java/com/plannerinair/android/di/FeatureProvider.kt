package com.plannerinair.android.di

import android.content.Context
import com.plannerinair.android.toplevel.logic.TopLevelFeature
import com.plannerinair.puerh.Feature
import com.plannerinair.puerh.SyncFeature

fun provideFeature(applicationContext: Context): Feature<TopLevelFeature.Msg, TopLevelFeature.State, TopLevelFeature.Eff> {
    return SyncFeature(
        TopLevelFeature.initialState(
        ),
        TopLevelFeature::reducer
    )
}
