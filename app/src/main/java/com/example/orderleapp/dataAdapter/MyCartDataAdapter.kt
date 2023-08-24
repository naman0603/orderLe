package com.example.orderleapp.dataAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderleapp.R
import com.example.orderleapp.dataModel.MyCartDataModel
import com.example.orderleapp.`object`.CartCountReceiverHolder
import com.example.orderleapp.ui.ImageViewActivity
import com.example.orderleapp.viewHolder.MyCartViewHolder
import com.google.gson.Gson

class MyCartDataAdapter(val context: Context, private val model: HashMap<String, MyCartDataModel>):
RecyclerView.Adapter<MyCartViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        return MyCartViewHolder(LayoutInflater.from(context).inflate(R.layout.my_cart_card_view,parent,false))
    }

    override fun getItemCount(): Int = model.size

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        val cartDataModel = model.values.elementAt(position)

        Glide.with(context).load(cartDataModel.imageUri).into(holder.itemView.findViewById(R.id.imgViewMyCart))
        holder.itemView.findViewById<TextView>(R.id.txtProductName).text = cartDataModel.name
        holder.itemView.findViewById<TextView>(R.id.txtCartWeight).text = (cartDataModel.weight.toFloat()*cartDataModel.counter.toInt()).toString()+" gms"
        holder.itemView.findViewById<TextView>(R.id.counter).text = cartDataModel.counter

        holder.itemView.findViewById<ImageView>(R.id.subCart).setOnClickListener {
            val counterText =  holder.itemView.findViewById<TextView>(R.id.counter)
            val counter =counterText.text.toString().toInt()
            val flag = 1

            if(counter == flag){
                counterText.text = "1"
                updateCounterInSharedPreferences(cartDataModel.id, 1)
                holder.itemView.findViewById<TextView>(R.id.txtCartWeight).text = (cartDataModel.weight.toFloat()).toString()+" gms"

            } else {
                val change = counter - 1
                counterText.text = change.toString()
                updateCounterInSharedPreferences(cartDataModel.id, change)
                holder.itemView.findViewById<TextView>(R.id.txtCartWeight).text = (cartDataModel.weight.toFloat()*change).toString()+" gms"
            }
        }

        holder.itemView.findViewById<ImageView>(R.id.imgViewMyCart).setOnClickListener {
            val intent = Intent(context, ImageViewActivity::class.java)
            intent.putExtra("URI", cartDataModel.imageUri)
            context.startActivity(intent)
        }

        holder.itemView.findViewById<ImageView>(R.id.addCart).setOnClickListener {
            val counterText =  holder.itemView.findViewById<TextView>(R.id.counter)
            val counter =counterText.text.toString().toInt()

            val change = counter + 1
            counterText.text = change.toString()

            updateCounterInSharedPreferences(cartDataModel.id, change)
            holder.itemView.findViewById<TextView>(R.id.txtCartWeight).text = (cartDataModel.weight.toFloat()*change).toString()+" gms"
        }
        holder.itemView.findViewById<ImageView>(R.id.remove).setOnClickListener {
            popup(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun popup(position: Int) {
        val cartDataModel = model.values.elementAt(position)
        val builder = AlertDialog.Builder(context,R.style.AlertDialogTheme)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage("Are You Sure You want To remove ${cartDataModel.name} from the Cart")
        builder.setIcon(R.drawable.ic_alert_box)

        //performing positive action
        builder.setPositiveButton("Yes"){ _, _ ->
            val cartData = model.values.elementAt(position)

            // Remove the data from the model
            model.values.remove(cartData)
            notifyDataSetChanged()

            // Remove the data from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("MyCartData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove(cartDataModel.id)
            editor.apply()

            CartCountReceiverHolder.sendCartCountChangedBroadcast(context)
        }
        builder.setNegativeButton("No"){_,_->}
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun updateCounterInSharedPreferences(key: String, counter: Int) {
        val sharedPreferences = context.getSharedPreferences("MyCartData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val cartDataJson = sharedPreferences.getString(key, null)
        if (cartDataJson != null) {
            val cartDataModel = Gson().fromJson(cartDataJson, MyCartDataModel::class.java)
            cartDataModel.counter = counter.toString()
            editor.putString(key, Gson().toJson(cartDataModel))
            editor.apply()
        }
    }

}