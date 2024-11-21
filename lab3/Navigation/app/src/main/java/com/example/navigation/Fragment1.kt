package com.example.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Fragment1 : Fragment(R.layout.fragment_1) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstNameEditText = view.findViewById<EditText>(R.id.firstName)
        val lastNameEditText = view.findViewById<EditText>(R.id.lastName)

        view.findViewById<Button>(R.id.button_to_fragment2).setOnClickListener {
            val bundle = Bundle().apply {
                putString("firstName", firstNameEditText.text.toString())
                putString("lastName", lastNameEditText.text.toString())
            }
            findNavController().navigate(R.id.action_fragment1_to_fragment2, bundle)
        }

        view.findViewById<Button>(R.id.button_to_fragment4).setOnClickListener {
            findNavController().navigate(R.id.action_fragment1_to_fragment4)
        }
    }
}