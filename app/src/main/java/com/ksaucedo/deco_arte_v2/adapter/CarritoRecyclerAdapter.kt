package com.ksaucedo.deco_arte_v2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.ksaucedo.deco_arte_v2.HomeActivity
import com.ksaucedo.deco_arte_v2.R
import com.ksaucedo.deco_arte_v2.model.Carrito
import kotlinx.android.synthetic.main.layout_carrito_list_item.view.*

class CarritoRecyclerAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Carrito>()
    var admin = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CarritoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_carrito_list_item, parent, false),
            admin
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CarritoViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(blogList: ArrayList<Carrito>) {
        items.clear()
        items.addAll(blogList)
        notifyDataSetChanged()
    }

    class CarritoViewHolder
    constructor(
        val view: View,
        private val admin: Boolean = false
    ) : RecyclerView.ViewHolder(view) {
        val descripcion = view.pdescripcionsss
        fun bind(carrito: Carrito) {
            descripcion.text = carrito.descripcion

            view.findViewById<ImageButton>(R.id.carritoEliminar).setOnClickListener {
                (view.context as HomeActivity).eliminarCarrito(carrito.id)
            }

        }
    }
}
