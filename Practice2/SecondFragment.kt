package com.example.practice2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.practice2.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(inflater, container, false)

        binding.nextFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.fragment2_to_fragment3)
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
}
