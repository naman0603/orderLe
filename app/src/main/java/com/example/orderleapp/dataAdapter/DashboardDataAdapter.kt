package com.example.orderleapp.dataAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.CategoryApiResponse
import com.example.orderleapp.dataModel.DashboardDataModel
import com.example.orderleapp.viewHolder.DashboardViewHolder

class DashboardDataAdapter(private val context: Context, private var model:ArrayList<CategoryApiResponse>)
    : RecyclerView.Adapter<DashboardViewHolder>(){
    var onItemClick : ((CategoryApiResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_items,parent,false))
    }

    override fun getItemCount(): Int = model.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.txtDashboardItems).text = model[position].categoryTitle
        Glide.with(context).load(model[position].categoryPictureUrl+model[position].categoryPicture).into(holder.itemView.findViewById(R.id.imgDashboardItems))

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(model[position])
        }
    }
}