package com.example.pogo_framework.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pogo_framework.R
import com.example.pogo_framework.helpers.Status
import com.example.pogo_framework.models.CapturedModel
import com.example.pogo_framework.ui.activities.DetailType
import com.example.pogo_framework.ui.activities.PokemonDetailView
import com.example.pogo_framework.ui.adapters.TeamRecyclerViewAdapter
import com.example.pogo_framework.view_models.CommonViewModel
import com.example.pogo_framework.view_models.TeamViewModel
import kotlinx.android.synthetic.main.fragment_team.*

class TeamFragment : Fragment() {

    lateinit var teamViewModel: TeamViewModel
    lateinit var commonViewModel: CommonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance((requireContext() as AppCompatActivity).application)
                .create(TeamViewModel::class.java)
        commonViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance((requireContext() as AppCompatActivity).application)
                .create(CommonViewModel::class.java)
        teamViewModel.getTeamInfo()
        attachObservers()
    }

    private fun attachObservers() {
        teamViewModel.teamInfoLiveData.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.LOADING) {
                showLoader()
            } else {
                hideLoader()
            }

            if (it == null && it?.message.isNullOrEmpty().not()) {
                Toast.makeText(
                    requireContext(),
                    it?.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                requireActivity().runOnUiThread {
                    it.data?.let { it -> initUi(it as ArrayList<CapturedModel>) }
                }
            }
        })
    }

    private fun showLoader() {
        progress_layout.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progress_layout.visibility = View.GONE
    }


    private fun initUi(capturedModelList: ArrayList<CapturedModel>) {
        rv_team.adapter = TeamRecyclerViewAdapter(
            capturedModelList,
            commonViewModel,
            this,
            { captureModel, transitionView ->
                PokemonDetailView.startActivity(
                    requireActivity(),
                    DetailType.CAPTURED,
                    -1,
                    capturedModel = captureModel,
                    transitionView = transitionView
                )
            })
        rv_team.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }
}