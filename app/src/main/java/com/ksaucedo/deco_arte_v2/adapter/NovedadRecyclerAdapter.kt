package com.ksaucedo.deco_arte_v2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.ksaucedo.deco_arte_v2.HomeActivity
import com.ksaucedo.deco_arte_v2.R
import com.ksaucedo.deco_arte_v2.model.Novedad
import kotlinx.android.synthetic.main.layout_novedad_list_item.view.*


class NovedadRecyclerAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Novedad>()
    var admin = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NovedadViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_novedad_list_item, parent, false),
            admin
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NovedadViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(novedades: ArrayList<Novedad>) {
        items.addAll(novedades)
    }

    class NovedadViewHolder
    constructor(
        val view: View,
        private val admin: Boolean = false
    ) : RecyclerView.ViewHolder(view) {
        val descripcion = view.pdescripcionsss
        fun bind(novedad: Novedad) {
            if (admin) {
                view.findViewById<ImageButton>(R.id.novedadEliminar).let {
                    it.visibility = View.VISIBLE
                    it.setOnClickListener {
                        (view.context as HomeActivity).eliminarNovedad(novedad.id)
                    }
                }

                view.findViewById<ImageButton>(R.id.novedadEditar).let {
                    it.visibility = View.VISIBLE
                    it.setOnClickListener {
                        (view.context as HomeActivity).ediatrNovedad(novedad.id)
                    }
                }
            }
            descripcion.text = novedad.texto
        }
    }
}
