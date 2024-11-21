package com.example.cookieapp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClickerViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(ClickerState())
    val stateFlow = _stateFlow.asStateFlow()
    // events: тоасты
    private var startTime = System.currentTimeMillis()
    private var clickCount = 0

    init {
        startPassiveIncome()
        startElapsedTimeTracker()
        startClickRateTracker()
    }

    private fun startElapsedTimeTracker() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                updateElapsedTime()
                updateAverageSpeed() // в минуту
            }
        }
    }


    @SuppressLint("DefaultLocale")
    private fun updateElapsedTime() {
        val elapsedMillis = System.currentTimeMillis() - startTime
        val hours = (elapsedMillis / 1000 / 3600).toInt()
        val minutes = ((elapsedMillis / 1000) % 3600 / 60).toInt()
        val seconds = (elapsedMillis / 1000 % 60).toInt()

        _stateFlow.value = _stateFlow.value.copy(
            elapsedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        )
    }

    private fun updateAverageSpeed() {
        val currentState = _stateFlow.value

        val activeSpeed = currentState.clickMultiplier * clickCount
        val passiveSpeed = currentState.passiveIncome

        _stateFlow.value = currentState.copy(
            averageSpeed = activeSpeed + passiveSpeed
        )
    }

    private fun startClickRateTracker() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _stateFlow.value
                val clicksPerSecond = clickCount

                clickCount = 0

                val activeSpeed = clicksPerSecond * currentState.clickMultiplier
                val passiveSpeed = currentState.passiveIncome
                _stateFlow.value = currentState.copy(
                    averageSpeed = activeSpeed + passiveSpeed
                )
            }
        }
    }

    fun onCookieClick() {
        _stateFlow.value = _stateFlow.value.copy(
            cookieCount = _stateFlow.value.cookieCount + _stateFlow.value.clickMultiplier
        )
        clickCount++
    }

    fun buyItem(item: ShopItem) {
        val currentState = _stateFlow.value

        if (currentState.cookieCount >= item.cost && item.currentLevel < item.maxLevel) {
            val updatedItems = currentState.shopItems.map { shopItem ->
                if (shopItem.name == item.name) {
                    shopItem.copy(
                        currentLevel = shopItem.currentLevel + 1,
                        cost = (shopItem.cost * 1.5).toInt()
                    )
                } else {
                    shopItem
                }
            }

            val newClickMultiplier = currentState.clickMultiplier + item.multiplier
            val newPassiveIncome = currentState.passiveIncome + item.passiveRate

            _stateFlow.value = currentState.copy(
                cookieCount = currentState.cookieCount - item.cost,
                clickMultiplier = newClickMultiplier,
                passiveIncome = newPassiveIncome,
                shopItems = updatedItems
            )
        }
    }

    private fun startPassiveIncome() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _stateFlow.value
                if (currentState.passiveIncome > 0) {
                    _stateFlow.value = currentState.copy(
                        cookieCount = currentState.cookieCount + currentState.passiveIncome
                    )
                }
            }
        }
    }
}
