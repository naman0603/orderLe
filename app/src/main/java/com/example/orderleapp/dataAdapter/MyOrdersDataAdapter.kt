package com.example.orderleapp.dataAdapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.MyOrdersApiResponse
import com.example.orderleapp.dataModel.MyOrdersDataModel
import com.example.orderleapp.viewHolder.MyOrdersViewHolder

@Suppress("DEPRECATION")
class MyOrdersDataAdapter(private val context: Context, private var model : ArrayList<MyOrdersApiResponse>):
    RecyclerView.Adapter<MyOrdersViewHolder>(){

    var onItemClick : ((MyOrdersApiResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        return MyOrdersViewHolder(LayoutInflater.from(context).inflate(R.layout.myorders_view,parent,false))
    }

    override fun getItemCount(): Int = model.size

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {



        holder.itemView.findViewById<TextView>(R.id.txtOrderNo).text= model[position].requestNumber

        holder.itemView.findViewById<ImageView>(R.id.imgEye).setOnClickListener {
            onItemClick?.invoke(model[position])
        }
    }
}