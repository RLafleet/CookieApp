package com.example.cookieapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, ClickerFragment())
                .commit()
        }

        observeToasts()
    }

    private val viewModel: ClickerViewModel by viewModels()

    private fun observeToasts() {
        lifecycleScope.launch {
            // Наблюдаем за потоком тостов
            viewModel.toastFlow.collectLatest { message ->
                Log.d("MainActivity", "Received toast message: $message")
                if (message.isNotEmpty()) {
                    // Показываем тост
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
