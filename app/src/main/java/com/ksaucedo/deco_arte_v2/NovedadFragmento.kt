package com.ksaucedo.deco_arte_v2

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class NovedadFragmento : Fragment(R.layout.fragment_inicio_fragmento) {
    var admin: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).initNovedadRecyclerView()
        if (admin) {
            view.findViewById<Button>(R.id.agregarNovedad).visibility = View.VISIBLE
        }
    }
}