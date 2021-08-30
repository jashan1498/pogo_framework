package com.example.pogo_framework.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pogo_framework.R
import com.example.pogo_framework.models.CapturedModel
import com.example.pogo_framework.ui.activities.DetailType
import com.example.pogo_framework.ui.activities.PokemonDetailView
import com.example.pogo_framework.ui.adapters.CapturedRecyclerViewAdapter
import com.example.pogo_framework.view_models.CapturedViewModel
import com.example.pogo_framework.view_models.CommonViewModel
import kotlinx.android.synthetic.main.fragment_captured.*


class CapturedFragment : Fragment() {

    private lateinit var capturedViewModel: CapturedViewModel
    private lateinit var commonViewModel: CommonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_captured, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        capturedViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance((requireContext() as AppCompatActivity).application)
                .create(CapturedViewModel::class.java)
        commonViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance((requireContext() as AppCompatActivity).application)
                .create(CommonViewModel::class.java)
        capturedViewModel.getCapturedPokemonList().observe(viewLifecycleOwner, {
            if (it.data == null && it.message.isNullOrEmpty().not()) {
                Toast.makeText(
                    requireContext(),
                    it.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                it.data?.let { it -> initUi(it) }
            }
        })
    }

    private fun initUi(capturedResponseModel: ArrayList<CapturedModel>) {
        rv_captured.adapter =
            CapturedRecyclerViewAdapter(
                capturedResponseModel,
                commonViewModel,
                this,
                { capturedModel, transitionView ->
                    PokemonDetailView.startActivity(
                        requireActivity(),
                        DetailType.CAPTURED,
                        -1,
                        capturedModel = capturedModel,
                        transitionView = transitionView
                    )
                })
        rv_captured.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
    }
}