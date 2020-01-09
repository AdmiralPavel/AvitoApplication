package com.example.avitoapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.example.avitoapplication.R

class SwitchViewAdapter( val data: ArrayList<Pair<String, Boolean>>) :
    RecyclerView.Adapter<SwitchViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val switch: Switch = view.findViewById(R.id.switchView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.switch_element, parent, false)
        return ViewHolder(layoutInflater)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.switch.text = data[position].first
        holder.switch.isChecked = data[position].second
        holder.switch.setOnCheckedChangeListener{ _, checked ->
            data[position] = Pair(data[position].first, checked)
        }
    }
}