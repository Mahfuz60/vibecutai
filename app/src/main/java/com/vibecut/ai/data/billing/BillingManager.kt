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
        // Play Billing one-time products (tip jar / donation)
        const val PRODUCT_DONATION_SMALL = "vibecut_support_small"
        const val PRODUCT_DONATION_MEDIUM = "vibecut_support_medium"
        const val PRODUCT_DONATION_LARGE = "vibecut_support_large"
    }

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    private val _availableProducts = MutableStateFlow(
        listOf(
            ProductDetails(PRODUCT_DONATION_SMALL, "Support Dev • Small", "One-time support", "$3"),
            ProductDetails(PRODUCT_DONATION_MEDIUM, "Support Dev • Medium", "One-time support", "$5"),
            ProductDetails(PRODUCT_DONATION_LARGE, "Support Dev • Large", "One-time support", "$10")
        )
    )
    val availableProducts: StateFlow<List<ProductDetails>> = _availableProducts

    fun initialize() {
        _isPremium.value = false
    }

    fun donationProducts(): List<ProductDetails> =
        _availableProducts.value.filter {
            it.productId == PRODUCT_DONATION_SMALL ||
                    it.productId == PRODUCT_DONATION_MEDIUM ||
                    it.productId == PRODUCT_DONATION_LARGE
        }

}