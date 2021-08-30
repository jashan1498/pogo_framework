package com.example.pogo_framework.ui.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pogo_framework.R
import com.example.pogo_framework.helpers.CommonHelpers
import com.example.pogo_framework.models.CapturedModel
import com.example.pogo_framework.view_models.CommonViewModel
import com.example.pogo_framework.view_models.TeamViewModel
import kotlinx.android.synthetic.main.team_item_view.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class TeamRecyclerViewAdapter(
    private val capturedModelList: ArrayList<CapturedModel>,
    private val teamViewModel: CommonViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val listener:(captureModel:CapturedModel,transitionView:View)->Unit
) :
    RecyclerView.Adapter<TeamRecyclerViewAdapter.TeamViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.team_item_view, parent, false)
        context = parent.context
        return TeamViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {

        val capturedModel = capturedModelList[position]
        context?.let {
            teamViewModel.getPokemon(capturedModel.name)
                .observe(lifecycleOwner, Observer { resource ->
                    Glide.with(it).load(resource.data?.sprites?.frontDefault)
                        .into(holder.itemView.imageView)
                    var pokemonType = ""
                    resource.data?.types?.forEach {
                        pokemonType += it.type.name + ","
                    }
                    if (pokemonType.endsWith(",")) {
                        pokemonType = pokemonType.substring(0, pokemonType.length - 1)
                    }
                    holder.itemView.pokemon_type.text = pokemonType
                })
        }
        holder.itemView.setOnClickListener {
            listener.invoke(capturedModel,holder.itemView.imageView)
        }
        holder.itemView.pokemon_name.text = capturedModel.name
        try {
            val date = capturedModel.capturedAt.split("T")[0]
            val localeDate = LocalDate.parse(date)

            holder.itemView.tv_captured_at.text = CommonHelpers.getFormattedDate(localeDate.toString()).toString()
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return capturedModelList.size
    }

    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}