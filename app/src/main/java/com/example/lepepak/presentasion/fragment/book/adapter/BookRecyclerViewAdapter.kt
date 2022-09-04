package com.example.lepepak.presentasion.fragment.book.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lepepak.presentasion.fragment.detail.DetailBottomDialogFragment
import com.example.lepepak.model.firebase.JavaneseDBResponse
import com.example.lepepak.R
import kotlinx.android.synthetic.main.itemcard_bookhome.view.*

class BookRecyclerViewAdapter: RecyclerView.Adapter<BookRecyclerViewAdapter.viewholder>() {
    var datalist = emptyList<JavaneseDBResponse>()

    class viewholder(view: View): RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.itemcard_bookhome,parent,false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val item = datalist[position]
        holder.itemView.tvbook_name.text = item.bentuk

        holder.itemView.setOnClickListener {
            val dialog = DetailBottomDialogFragment()
            val supportFragment = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val args = Bundle()
            args.putString("nama",item.bentuk)
            args.putString("desc",item.desc)
            args.putString("link",item.link)
            dialog.setArguments(args)
            dialog.show(supportFragment,"dialog")
        }

        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(item.link)
            .into(holder.itemView.img_book)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    fun setdata(list : List<JavaneseDBResponse>){
        datalist = list
        notifyDataSetChanged()
    }
}