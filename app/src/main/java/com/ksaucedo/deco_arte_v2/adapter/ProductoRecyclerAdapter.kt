package com.ksaucedo.deco_arte_v2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.ksaucedo.deco_arte_v2.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ksaucedo.deco_arte_v2.HomeActivity
import com.ksaucedo.deco_arte_v2.model.Producto
import kotlinx.android.synthetic.main.layout_product_list_item.*
import kotlinx.android.synthetic.main.layout_product_list_item.view.*

import kotlin.collections.ArrayList

class ProductoRecyclerAdapter(homeActivity: HomeActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<Producto>()
    var admin = false
    var homeActivity = homeActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_product_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is ProductoViewHolder -> {
                holder.bind(items[position])
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(blogList: List<Producto>) {
        items.clear()
        items.addAll(blogList)
        notifyDataSetChanged()
    }

    class ProductoViewHolder
    constructor(
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        val imagen = view.imagen
        val descripcion = view.descripcion
        val precio = view.precio


        fun bind(producto: Producto) {

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(producto.imagen)
                .into(imagen)
            descripcion.text = producto.descripcion
            precio.text = producto.precio

            view.findViewById<Button>(R.id.agregarCarrito).setOnClickListener {
                (view.context as HomeActivity).agregarCarrito(producto)
            }
        }

    }

}
