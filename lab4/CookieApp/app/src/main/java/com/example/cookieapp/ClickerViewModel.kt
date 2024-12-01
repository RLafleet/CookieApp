package com.example.cookieapp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ClickerViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(ClickerState())
    val stateFlow = _stateFlow.asStateFlow()
    // events: toasts
    // shared flow for toasts
    private var startTime = System.currentTimeMillis()
    private var clickCount = 0

    private val _toastFlow = MutableSharedFlow<String>(replay = 1)
    val toastFlow = _toastFlow.asSharedFlow()

    private val _cookieCount = MutableStateFlow(0)
    val cookieCount: StateFlow<Int> get() = _cookieCount

    fun updateCookieCount(count: Int) {
        _cookieCount.value = count
    }

    private val clickHistory = IntArray(60)
    private var historyIndex = 0

    init {
        startPassiveIncome()
        startElapsedTimeTracker()
        startClickRateTracker()
        startToastGeneration()
    }

    private fun startElapsedTimeTracker() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                updateElapsedTime()
                updateAverageSpeed()
                updateAverageSpeedPerMinute()
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateElapsedTime() {
        val elapsedMillis = System.currentTimeMillis() - startTime
        val minutes = ((elapsedMillis / 1000) % 3600 / 60).toInt()
        val seconds = (elapsedMillis / 1000 % 60).toInt()

        _stateFlow.value = _stateFlow.value.copy(
            elapsedTime = String.format("%2d:%02d", minutes, seconds)
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
    private fun startToastGeneration() {
        viewModelScope.launch {
            cookieCount.collect { currentCookie ->
                if (currentCookie !== 0 && currentCookie % 20 == 0) {
                    generateRandomToast(currentCookie)
                }
            }
        }
    }


    private fun updateAverageSpeedPerMinute() {
        val currentState = _stateFlow.value

        val totalClicks = clickHistory.sum()
        val activeSpeed = totalClicks * currentState.clickMultiplier
        val passiveSpeed = currentState.passiveIncome * 60

        _stateFlow.value = currentState.copy(
            averageSpeedMin = activeSpeed + passiveSpeed
        )

        historyIndex = (historyIndex + 1) % 60
        clickHistory[historyIndex] = 0
    }


    private fun generateRandomToast(cookies: Int) {
        val randomMessages = listOf(
            "Wow, you have $cookies cookies! Keep it up!",
            "Nice job! You've reached $cookies cookies!",
            "You're on fire! $cookies cookies and counting!",
            "Amazing! $cookies cookies, you're unstoppable!"
        )
        val randomMessage = randomMessages[Random.nextInt(randomMessages.size)]
        viewModelScope.launch(Dispatchers.Main) {
            _toastFlow.emit(randomMessage)
        }
    }

    private fun startClickRateTracker() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _stateFlow.value
                val clicksPerMinute = clickCount

                clickCount = 0

                val activeSpeed = clicksPerMinute * currentState.clickMultiplier
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
        val newCookieCount = _stateFlow.value.cookieCount + _stateFlow.value.clickMultiplier
        updateCookieCount(newCookieCount)
        clickCount++
        clickHistory[historyIndex]++
    }

    private var purchasedAtLeastOne = false

    fun buyItem(item: ShopItem) {
        val currentState = _stateFlow.value

        if (currentState.cookieCount >= item.cost && item.currentLevel < item.maxLevel) {
            val updatedItems = currentState.shopItems.map { shopItem ->
                if (shopItem.name == item.name) {
                    shopItem.copy(
                        currentLevel = shopItem.currentLevel + 1,
                        cost = (shopItem.cost * 1.5).toInt()
                    )
                } else shopItem
            }

            val newClickMultiplier = currentState.clickMultiplier + item.multiplier
            val newPassiveIncome = currentState.passiveIncome + item.passiveRate

            purchasedAtLeastOne = true

            _stateFlow.value = currentState.copy(
                cookieCount = currentState.cookieCount - item.cost,
                clickMultiplier = newClickMultiplier,
                passiveIncome = newPassiveIncome,
                shopItems = updatedItems
            )
        }
    }


    fun getCurrentCookieCount(): Int {
        val cookies = _stateFlow.value.cookieCount
        return cookies
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
                    val newCookieCount = currentState.cookieCount + currentState.passiveIncome
                    updateCookieCount(newCookieCount)
                }
            }
        }
    }
}
