package com.saebyeol.weather

import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main.view.*

class MainAdapter(private val items: ArrayList<MainData>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: ${item.test}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MainAdapter.MainViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return MainAdapter.MainViewHolder(inflatedView)

    }

    class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: MainData) {
            view.tv_id1.text = item.test
            view.tv_id2.text = item.test1
            view.tv_id3.text = item.test2
            view.tv_id4.text = item.test3
        }

    }
}