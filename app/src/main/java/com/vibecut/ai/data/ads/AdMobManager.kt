package com.vibecut.ai.data.ads

import android.app.Activity
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _rewardedAdReady = MutableStateFlow(false)
    val rewardedAdReady: StateFlow<Boolean> = _rewardedAdReady

    fun loadRewardedAd() {
        _rewardedAdReady.value = false
    }

    fun showRewardedAd(
        activity: Activity,
        onRewarded: (Int) -> Unit,
        onDismissed: () -> Unit
    ) {
        onRewarded(5)
        onDismissed()
    }

    fun loadInterstitialAd() {
    }

    fun showInterstitialAd(
        activity: Activity,
        onDismissed: () -> Unit
    ) {
        onDismissed()
    }
}