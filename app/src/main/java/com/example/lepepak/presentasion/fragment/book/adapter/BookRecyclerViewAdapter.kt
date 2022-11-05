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

class BookRecyclerViewAdapter: RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>() {
    private var dataList = emptyList<JavaneseDBResponse>()
    private lateinit var onItemCLickDetail : OnItemClickDetail

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {}

    fun onItemClickDetail(onItemClickDetail: OnItemClickDetail){
        this.onItemCLickDetail = onItemClickDetail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.itemcard_bookhome,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.itemView.tvbook_name.text = item.bentuk

        holder.itemView.img_book.setOnClickListener {
            onItemCLickDetail.onItemClickDetail(item)
        }

        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(item.link)
            .into(holder.itemView.img_book)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(list : List<JavaneseDBResponse>){
        dataList = list
        notifyDataSetChanged()
    }

    interface OnItemClickDetail{
        fun onItemClickDetail(data : JavaneseDBResponse)
    }
}