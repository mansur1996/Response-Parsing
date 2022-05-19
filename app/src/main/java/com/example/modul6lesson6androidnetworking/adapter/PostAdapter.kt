package com.example.modul6lesson6androidnetworking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.modul6lesson6androidnetworking.R
import com.example.modul6lesson6androidnetworking.activity.MainActivity
import com.example.modul6lesson6androidnetworking.model.Post

class PostAdapter (val activity : MainActivity, val items : ArrayList<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PosterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_poster_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post : Post = items[position]
        if(holder is PosterViewHolder){
            holder.apply {

                tv_title.text = post.title.toUpperCase()
                tv_body.text = post.body

                ll_post.setOnLongClickListener {
                    activity.dialogPost(post)
                    return@setOnLongClickListener false
                }

                deleteIv.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class PosterViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val ll_post: LinearLayout = view.findViewById(R.id.ll_post)
        val tv_title = view.findViewById<TextView>(R.id.tv_title)
        val tv_body = view.findViewById<TextView>(R.id.tv_body)
        val deleteIv = view.findViewById<ImageView>(R.id.iv_delete)
    }
}