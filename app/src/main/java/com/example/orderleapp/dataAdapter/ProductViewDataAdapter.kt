package com.example.orderleapp.dataAdapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.orderleapp.MyApplication
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataModel.MyCartDataModel
import com.example.orderleapp.`object`.CartCountReceiverHolder
import com.example.orderleapp.ui.ImageViewActivity
import com.example.orderleapp.viewHolder.ProductViewViewHolder
import com.google.gson.Gson

class ProductViewDataAdapter(val context: Context, val model : ArrayList<ProductApiResponse>):
    RecyclerView.Adapter<ProductViewViewHolder>(){

    var onItemClick: ((ProductApiResponse) -> Unit)? = null
    var boolean: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewViewHolder {
        return ProductViewViewHolder(LayoutInflater.from(context).inflate(R.layout.product_view_card_view,parent,false))
    }

    override fun getItemCount(): Int = model.size

    override fun onBindViewHolder(holder: ProductViewViewHolder, position: Int) {
        val animation = holder.itemView.findViewById<LottieAnimationView>(R.id.lottieAnimation)
        val dbHelper = MyApplication.databaseHelper
        val productApiResponses = dbHelper.getAllProductApiResponses()
        for (response in productApiResponses){
            if(response.productId == model[position].productId){
                animation.playAnimation()
                boolean = 1
                break
            }
        }
        val charge = "Stone Charges :- " +"<b>₹ </b>"+ model[position].totalStoneCharge
        val weight ="Weight approx :- " + model[position].productWeight +" gms"
        holder.itemView.findViewById<TextView>(R.id.txtWeight).text = Html.fromHtml(weight)
        holder.itemView.findViewById<TextView>(R.id.txtCharge).text = Html.fromHtml(charge)
        holder.itemView.findViewById<TextView>(R.id.txtItemName).text = model[position].productName

        Glide.with(context).load(model[position].productPictureUrl+model[position].productPicture).into(holder.itemView.findViewById(R.id.imgPurchase))

        animation.setOnClickListener {
            if (boolean==0) {
                animation.playAnimation()
                boolean = 1
                val dbHelper = MyApplication.databaseHelper
                val id = dbHelper.insertProductApiResponse(model[position].productId,model[position].productName,model[position])
            }else{
                boolean = 0
                animation.cancelAnimation()
                animation.progress = 0f
                val dbHelper = MyApplication.databaseHelper
                val id = dbHelper.removeProductApiResponse(model[position].productId)
            }
        }
        holder.itemView.setOnClickListener {
            val item = model[position]
            onItemClick?.invoke(item)
        }
        holder.itemView.findViewById<Button>(R.id.btnAddToCartProductView).setOnClickListener {
            val counterText =  holder.itemView.findViewById<TextView>(R.id.txtCounterProductView)
            val counter =counterText.text.toString().toInt()
            val modelData = model[position]
            if (counter>0){
                addToCart(counter,modelData,model,holder,position)
                counterText.text = "1"
            }else{
                Toast.makeText(context, "Please Add Items", Toast.LENGTH_SHORT).show()
            }

        }
        holder.itemView.findViewById<ImageView>(R.id.imgPurchase).setOnClickListener {
            val imageUrls = ArrayList<String>()
            if (model[position].productTypeBeans.size > 0) {
                imageUrls.add(model[position].productPictureUrl+model[position].productPicture)
                for (productTypeBean in model[position].productTypeBeans) {
                    imageUrls.add(model[position].productPictureUrl+productTypeBean.productImage)
                }
            }else{
                imageUrls.add(model[position].productPictureUrl+model[position].productPicture)
            }
            val intent = Intent(context, ImageViewActivity::class.java)
            intent.putStringArrayListExtra("URIs", imageUrls)
            context.startActivity(intent)

        }
        holder.itemView.findViewById<ImageView>(R.id.subProductView).setOnClickListener {
            val counterText =  holder.itemView.findViewById<TextView>(R.id.txtCounterProductView)
            val counter =counterText.text.toString().toInt()
            val flag = 1

            if(counter == flag){
                counterText.text = "1"
                val weight = "Weight approx :- " +  (model[position].productWeight.toFloat()).toString()+" gms"
                holder.itemView.findViewById<TextView>(R.id.txtWeight).text = Html.fromHtml(weight)
            }else{
                val change = counter - 1
                counterText.text = change.toString()
                val weight = "Weight approx :- " +  (model[position].productWeight.toFloat()*change).toString()+" gms"
                holder.itemView.findViewById<TextView>(R.id.txtWeight).text = Html.fromHtml(weight)
            }
        }
        holder.itemView.findViewById<ImageView>(R.id.addProductView).setOnClickListener {
            val counterText =  holder.itemView.findViewById<TextView>(R.id.txtCounterProductView)
            val counter =counterText.text.toString().toInt()

            val change = counter + 1

            counterText.text = change.toString()
            val weight = "Weight approx :- " +  (model[position].productWeight.toFloat()*change).toString()+" gms"
            holder.itemView.findViewById<TextView>(R.id.txtWeight).text = Html.fromHtml(weight)
        }
    }

    private fun addToCart(
        counter: Int,
        modelData: ProductApiResponse,
        model: ArrayList<ProductApiResponse>,
        holder: ProductViewViewHolder,
        position: Int
    ) {
        val sharedPreferences = context.getSharedPreferences("MyCartData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val itemKey = modelData.productId.toString()
        val existingCartDataJson = sharedPreferences.getString(itemKey, null)

        if (existingCartDataJson != null) {
            // If item already exists, update its quantity
            val existingCartDataModel = Gson().fromJson(existingCartDataJson, MyCartDataModel::class.java)
            val updatedCounter = existingCartDataModel.counter.toInt() + counter
            existingCartDataModel.counter = updatedCounter.toString()

            val updatedCartDataJson = Gson().toJson(existingCartDataModel)
            editor.putString(itemKey, updatedCartDataJson)
            editor.apply()

            val weight ="Weight approx :- " + this.model[position].productWeight +" gms"
            holder.itemView.findViewById<TextView>(R.id.txtWeight).text = Html.fromHtml(weight)

            Toast.makeText(context, "Quantity updated in cart", Toast.LENGTH_SHORT).show()
        } else {
            // If item does not exist, add it to the cart
            val cartDataModel = MyCartDataModel(
                modelData.productId.toString(), modelData.productPictureUrl + modelData.productPicture,
                modelData.productName, modelData.stoneCharge, modelData.productWeight.toString(), counter.toString(),
                modelData.categoryName,modelData.ringSize
            )

            val gson = Gson()
            val cartDataJson = gson.toJson(cartDataModel)

            editor.putString(itemKey, cartDataJson)
            editor.apply()
            val weight ="Weight approx :- " + this.model[position].productWeight +" gms"
            holder.itemView.findViewById<TextView>(R.id.txtWeight).text = Html.fromHtml(weight)
            CartCountReceiverHolder.sendCartCountChangedBroadcast(context)
        }
    }
}