package com.example.cookieapp

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
import kotlinx.coroutines.flow.collect
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

        val shopAdapter = ShopAdapter { item -> viewModel.buyItem(item) }
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

        binding.clickButton.setOnClickListener {
            viewModel.onCookieClick()
        }

        binding.openClickerButton.setOnClickListener {
            binding.clickerView.visibility = View.VISIBLE
            binding.shopView.visibility = View.GONE
        }

        binding.openShopButton.setOnClickListener {
            binding.clickerView.visibility = View.GONE
            binding.shopView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}