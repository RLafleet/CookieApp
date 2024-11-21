package com.example.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Fragment2 : Fragment(R.layout.fragment_2) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstName = arguments?.getString("firstName") ?: "Имя не указано"
        val lastName = arguments?.getString("lastName") ?: "Фамилия не указана"

        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = "Имя: $firstName\nФамилия: $lastName"

        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)

        view.findViewById<Button>(R.id.button_next).setOnClickListener {
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1
            val year = datePicker.year

            val birthDate = "$day/$month/$year"

            val bundle = Bundle().apply {
                putString("firstName", firstName)
                putString("lastName", lastName)
                putString("birthDate", birthDate)
            }
            findNavController().navigate(R.id.action_fragment2_to_fragment3, bundle)
        }
    }
}