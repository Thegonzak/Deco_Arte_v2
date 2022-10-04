package com.ksaucedo.deco_arte_v2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_contactar_fragmento.*
import java.net.URLEncoder


class CarritoFragmento : Fragment(R.layout.fragment_contactar_fragmento) {

    lateinit var numero: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enviarWhatsApp.setOnClickListener {
            val br = "%0a"
            var text = "Hola, estoy interesado en los productos de su tienda\n\n"
            text += getTemplate()

            text = URLEncoder.encode(text, "utf-8")
            val url = "https://wa.me/+$numero?text=$text"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            (activity as HomeActivity).limpiarCarrito()
            startActivity(i)
        }

        (activity as HomeActivity).initCarritoRecyclerView()
    }

    fun getTemplate(): String {
        val carritosList = (activity as HomeActivity).getCarritosList()
        var text = ""
        for (carrito in carritosList) {
            text += """
                ====================
                Id: ${carrito.id}
                Producto: ${carrito.descripcion}
                Precio: ${carrito.precio}
                
            """.trimIndent()
        }
        return text
    }


}