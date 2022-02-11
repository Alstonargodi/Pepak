package com.example.lepepak.Util.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lepepak.Detail_bottom
import com.example.lepepak.Model.Firebasedb
import com.example.lepepak.R
import kotlinx.android.synthetic.main.cv_bookhome.view.*

class Rvbookadapter: RecyclerView.Adapter<Rvbookadapter.viewholder>() {
    var datalist = emptyList<Firebasedb>()

    class viewholder(view: View): RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.cv_bookhome,parent,false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val item = datalist[position]
        holder.itemView.tvbook_name.text = item.bentuk

        holder.itemView.btn_moredetail.setOnClickListener {
            val dialog = Detail_bottom()
            val spfragment = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val args = Bundle()
            args.putString("nama",item.bentuk)
            args.putString("desc",item.desc)
            args.putString("link",item.link)
            dialog.setArguments(args)
            dialog.show(spfragment,"dialog")
        }

        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(item.link)
            .into(holder.itemView.img_book)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    fun setdata(list : List<Firebasedb>){
        datalist = list
        notifyDataSetChanged()
    }
}