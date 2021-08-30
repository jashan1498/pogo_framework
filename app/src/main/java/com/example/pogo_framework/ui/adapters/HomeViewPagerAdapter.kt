package com.example.pogo_framework.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pogo_framework.ui.fragments.CapturedFragment
import com.example.pogo_framework.ui.fragments.CommunityFragment
import com.example.pogo_framework.ui.fragments.ExploreFragment
import com.example.pogo_framework.ui.fragments.TeamFragment


class HomeViewPagerAdapter(fm: FragmentManager, activity: AppCompatActivity) :
    FragmentStateAdapter(fm, activity.lifecycle) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ExploreFragment()
            }
            1 -> {
                CommunityFragment()
            }
            2 -> {
                TeamFragment()
            }
            else -> {
                CapturedFragment()
            }
        }
    }

}