package com.example.pogo_framework.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pogo_framework.R
import com.example.pogo_framework.helpers.CommonHelpers
import com.example.pogo_framework.helpers.Status
import com.example.pogo_framework.models.Friends
import com.example.pogo_framework.view_models.CommonViewModel
import kotlinx.android.synthetic.main.community_activity_view.view.*


class ActivityRecyclerViewAdapter(
    private val communityList: ArrayList<Friends>,
    private val commonViewModel: CommonViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val listener: (friendModel: Friends,transitionView:View) -> Unit
) :
    RecyclerView.Adapter<ActivityRecyclerViewAdapter.ActivityViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.community_activity_view, parent, false)
        context = parent.context
        return ActivityViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {

        val communityModel = communityList[position]
        holder.itemView.activity_text.text =
            "Trainer ${communityModel.name} captured ${communityModel.pokemon.name} ${
                CommonHelpers.getRelativeTime(communityModel.pokemon.capturedAt)
            }"

        context?.let {
            commonViewModel.getPokemon(communityModel.pokemon.name.lowercase())
                .observe(lifecycleOwner, { resource ->
                    if (resource.status == Status.SUCCESS) {
                        Glide.with(it).load(resource.data?.sprites?.frontDefault)
                            .into(holder.itemView.imageView)
                    }
                })

        }
        holder.itemView.setOnClickListener {
            listener.invoke(communityModel,holder.itemView.imageView)
        }
    }

    override fun getItemCount(): Int {
        return communityList.size
    }

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}