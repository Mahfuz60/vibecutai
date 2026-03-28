package com.vibecut.ai.data.billing

import android.app.Activity
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class ProductDetails(
    val productId: String,
    val title: String = "",
    val description: String = "",
    val price: String = ""
)

@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val PRODUCT_PREMIUM_MONTHLY = "vibecut_premium_monthly"
        const val PRODUCT_PREMIUM_YEARLY = "vibecut_premium_yearly"
    }

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    private val _availableProducts = MutableStateFlow(
        listOf(
            ProductDetails(PRODUCT_PREMIUM_MONTHLY, "Monthly Premium", "Local placeholder", "$0"),
            ProductDetails(PRODUCT_PREMIUM_YEARLY, "Yearly Premium", "Local placeholder", "$0")
        )
    )
    val availableProducts: StateFlow<List<ProductDetails>> = _availableProducts

    fun initialize() {
        _isPremium.value = false
    }

    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails) {
        _isPremium.value = true
    }
}