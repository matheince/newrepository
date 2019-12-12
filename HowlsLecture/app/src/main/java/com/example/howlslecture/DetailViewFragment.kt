package com.example.howlslecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_detail.view.*

class  DetailViewFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_detail,container,false)
        view.detailview_recyclerview.adapter = DetailRecyclerViewAdapter()
        view.detailview_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view

    }
    inner class DetailRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomDetailHolder(view)
        }

        private inner class CustomDetailHolder(view:View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as CustomDetailHolder).itemView
        }

    }
}