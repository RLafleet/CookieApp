package com.example.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Fragment4 : Fragment(R.layout.fragment_4) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button_to_fragment5).setOnClickListener {
            findNavController().navigate(R.id.action_fragment4_to_fragment5)
        }

        view.findViewById<Button>(R.id.button_to_fragment1).setOnClickListener {
            findNavController().navigate(R.id.action_fragment4_to_fragment1)
        }
    }
}