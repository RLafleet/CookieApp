package com.example.cookieapp

import ShopAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookieapp.databinding.FragmentClickerBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ClickerFragment : Fragment() {
    private var _binding: FragmentClickerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shopAdapter = ShopAdapter(
            onBuyClick = { item ->
                viewModel.buyItem(item)
            },
            getCurrentCookies = { viewModel.stateFlow.value.cookieCount },
        )

        binding.shopRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.shopRecyclerView.adapter = shopAdapter

        viewModel.stateFlow.onEach { state ->
            binding.cookieCountText.text = "Cookies: ${state.cookieCount}"
            binding.cookiesPerSecondText.text = "Cookies/sec: ${state.passiveIncome}"
            binding.cookiesPerClickText.text = "Cookies/click: ${state.clickMultiplier}"
            binding.elapsedTimeText.text = "Time: ${state.elapsedTime}"
            binding.averageSpeedText.text = "Avg speed: ${state.averageSpeed}"

            shopAdapter.submitList(state.shopItems)
        }.launchIn(lifecycleScope)

        binding.cookieImage.setOnClickListener {
            viewModel.onCookieClick()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_clicker -> {
                    binding.clickerView.visibility = View.VISIBLE
                    binding.shopView.visibility = View.GONE
                    true
                }
                R.id.nav_shop -> {
                    binding.clickerView.visibility = View.GONE
                    binding.shopView.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }

        binding.cookieImage.setOnClickListener { v ->
            v.animate()
                .scaleX(0.85f)
                .scaleY(0.85f)
                .setDuration(10)
                .withEndAction {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100)
                }
            viewModel.onCookieClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

