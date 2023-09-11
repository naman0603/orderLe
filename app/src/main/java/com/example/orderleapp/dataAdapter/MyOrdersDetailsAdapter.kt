package com.example.orderleapp.dataAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.MyOrdersApiResponse
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataModel.MyOrdersDataModel
import com.example.orderleapp.ui.ImageViewActivity
import com.example.orderleapp.viewHolder.MyOrdersDetailsViewHolder
import com.example.orderleapp.viewHolder.MyOrdersViewHolder

@Suppress("DEPRECATION")
class MyOrdersDetailsAdapter(private val context: Context, private var model : ArrayList<ProductApiResponse>):
    RecyclerView.Adapter<MyOrdersDetailsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersDetailsViewHolder {
        return MyOrdersDetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.my_oreders_detail_card_view,parent,false))
    }

    override fun getItemCount(): Int = model.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyOrdersDetailsViewHolder, position: Int) {

        val name = "<b>${model[position].productName}</b>"
        val quantity = "<b>Qty :- </b>${ model[position].quantity}"
        val gold = "<b>Gold :- </b>${ model[position].goldCarat}"

        holder.itemView.findViewById<TextView>(R.id.txtId).text= Html.fromHtml(name)
        holder.itemView.findViewById<TextView>(R.id.txtWeightOrder).text= "Weight approx :- ${model[position].productWeight}"
        holder.itemView.findViewById<TextView>(R.id.txtQuantity).text=Html.fromHtml(quantity)
        holder.itemView.findViewById<TextView>(R.id.txtGold).text=Html.fromHtml(gold)

        Glide.with(context).load(model[position].productPictureUrl+model[position].productPicture).into(
            holder.itemView.findViewById(R.id.imgViewOrders)
        )
        holder.itemView.findViewById<ImageView>(R.id.imgViewOrders).setOnClickListener {
            val uri = model[position].productPictureUrl+model[position].productPicture

            val intent = Intent(context, ImageViewActivity::class.java)
            intent.putExtra("URI", uri)
            context.startActivity(intent)
        }
    }
}