package com.ksaucedo.deco_arte_v2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText


class ProductoFragmento : Fragment(R.layout.fragment_producto_fragmento) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val findViewById = view.findViewById<EditText>(R.id.txtbuscar)
        view.findViewById<Button>(R.id.buscar).setOnClickListener {
            (activity as HomeActivity).filterProductos(findViewById.text.toString())
        }
        (activity as HomeActivity).initProductoRecyclerView()
    }

}