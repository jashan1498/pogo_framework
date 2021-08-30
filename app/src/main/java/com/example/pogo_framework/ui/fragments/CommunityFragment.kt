package com.example.pogo_framework.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pogo_framework.R
import com.example.pogo_framework.models.ActivityModelResponse
import com.example.pogo_framework.models.Friends
import com.example.pogo_framework.ui.activities.DetailType
import com.example.pogo_framework.ui.activities.PokemonDetailView
import com.example.pogo_framework.ui.adapters.ActivityRecyclerViewAdapter
import com.example.pogo_framework.view_models.CommonViewModel
import com.example.pogo_framework.view_models.CommunityViewModel
import kotlinx.android.synthetic.main.custom_trainer_activity_view.view.*
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {

    lateinit var communityViewModel: CommunityViewModel
    lateinit var commonViewModel: CommonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communityViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance((requireContext() as AppCompatActivity).application)
                .create(CommunityViewModel::class.java)
        commonViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance((requireContext() as AppCompatActivity).application)
                .create(CommonViewModel::class.java)
        communityViewModel.getProfileActivity().observe(viewLifecycleOwner, {
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

    private fun initUi(activityModelResponse: ActivityModelResponse) {
        activityModelResponse.friends?.let {
            addView("Friends", it)
        }
        activityModelResponse.foes?.let {
            addView("Foes", it)
        }
    }

    private fun addView(title: String, list: ArrayList<Friends>) {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_trainer_activity_view, null, false)

        view.activity_title.text = title
        view.recycler_view.adapter =
            ActivityRecyclerViewAdapter(
                list,
                commonViewModel,
                requireActivity(),
                { friendModel, transitionView ->
                    PokemonDetailView.startActivity(
                        requireActivity(),
                        DetailType.CAPTURED,
                        -1,
                        capturedByOtherModel = friendModel,
                        transitionView = transitionView
                    )
                })
        view.recycler_view.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        parent.addView(view)
    }
}