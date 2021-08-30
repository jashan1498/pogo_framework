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
import com.example.pogo_framework.models.CapturedModel
import com.example.pogo_framework.view_models.CommonViewModel
import kotlinx.android.synthetic.main.captured_view.view.*
import java.util.*


class CapturedRecyclerViewAdapter(
    private val capturedModelList: ArrayList<CapturedModel>,
    private val commonViewModel: CommonViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val listener: (captureModel: CapturedModel,transitionView:View) -> Unit
) :
    RecyclerView.Adapter<CapturedRecyclerViewAdapter.CapturedViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapturedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.captured_view, parent, false)
        context = parent.context
        return CapturedViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CapturedViewHolder, position: Int) {

        val capturedModel = capturedModelList[position]
        context?.let {
            commonViewModel.getPokemon(capturedModel.name)
                .observe(lifecycleOwner, Observer { resource ->
                    Glide.with(it).load(resource.data?.sprites?.frontDefault)
                        .into(holder.itemView.imageView)
                })
        }
        holder.itemView.setOnClickListener {
            listener.invoke(capturedModel,holder.itemView.imageView)
        }
    }

    override fun getItemCount(): Int {
        return capturedModelList.size
    }

    class CapturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}