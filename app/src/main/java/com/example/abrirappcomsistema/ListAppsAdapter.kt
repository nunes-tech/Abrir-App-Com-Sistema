package com.example.abrirappcomsistema

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.abrirappcomsistema.databinding.ItemAppBinding

class ListAppsAdapter(
    val salvarApp: ( packageName: String )-> Unit
) : Adapter<ListAppsAdapter.ListAppsViewHolder>() {

    private var listaApps: List<App> = emptyList()

    fun updateList(newList: List<App>) {
        listaApps = newList
        notifyDataSetChanged()
    }

    inner class ListAppsViewHolder(
        val binding: ItemAppBinding
    ) : ViewHolder( binding.root ) {

        fun bind(position: Int) {
            val nome = listaApps[position].name
            val icon = listaApps[position].icon
            binding.layout.setOnClickListener {
                salvarApp( listaApps[position].packageName )
            }
            binding.appName.text = nome
            binding.appIcon.setImageDrawable( icon )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAppsViewHolder {
        val layoutInflater = LayoutInflater.from( parent.context )
        val binding = ItemAppBinding.inflate( layoutInflater, parent, false)
        return ListAppsViewHolder( binding )
    }

    override fun getItemCount(): Int {
        return listaApps.size
    }

    override fun onBindViewHolder(holder: ListAppsViewHolder, position: Int) {
        holder.bind(position)
    }
}