package com.example.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Fragment3 : Fragment(R.layout.fragment_3) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstName = arguments?.getString("firstName") ?: "Имя не указано"
        val lastName = arguments?.getString("lastName") ?: "Фамилия не указана"
        val birthDate = arguments?.getString("birthDate") ?: "Дата рождения не указана"

        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = "Имя: $firstName\nФамилия: $lastName\nДата рождения: $birthDate"

        view.findViewById<Button>(R.id.button_next).setOnClickListener {
            findNavController().navigate(R.id.action_fragment3_to_fragment1)
        }
    }
}