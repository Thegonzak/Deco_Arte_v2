package com.ksaucedo.deco_arte_v2

import android.content.Intent
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ksaucedo.deco_arte_v2.model.Usuario
import kotlinx.android.synthetic.main.fragment_cuenta_fragmento.*


class CuentaFragmento(var usuario: Usuario?) : Fragment(R.layout.fragment_cuenta_fragmento) {

    private var email: String? = null
    private lateinit var arrayCiudad: ArrayAdapter<String>
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onStart() {
        super.onStart()
        arrayCiudad = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item
        )
        FirebaseAuth.getInstance().currentUser?.let {
            email = it.email
        }

        db.collection("ciudad").get().addOnSuccessListener { result ->
            for (document in result) {
                arrayCiudad.add(document.get("nombre").toString())
            }
            ciudadSpinner.adapter = arrayCiudad

            if (usuario != null) {
                cedulaText.setText(usuario!!.cedula)
                nombreText.setText(usuario!!.nombre)
                apellidosText.setText(usuario!!.apellido)
                ciudadSpinner.setSelection(arrayCiudad.getPosition(usuario!!.ciudad))
                direccionText.setText(usuario!!.direccion)
                fechaNac.setText(usuario!!.fechaNac)
                telefonoPhone.setText(usuario!!.telefono)
            }

        }


        cerrarSessionButton.setOnClickListener {
            (activity as HomeActivity).cerrarSesion()
        }

        btnGuardar.setOnClickListener {
            Guardar()
        }
    }

    private fun Guardar() {
        if (cedulaText.text.isNotBlank() &&
            nombreText.text.isNotBlank() &&
            apellidosText.text.isNotBlank() &&
            direccionText.text.isNotBlank() &&
            fechaNac.text.isNotBlank() &&
            telefonoPhone.text.isNotBlank()
        ) {
            val datos = hashMapOf(
                "cedula" to cedulaText.text.toString(),
                "nombre" to nombreText.text.toString(),
                "apellido" to apellidosText.text.toString(),
                "ciudad" to ciudadSpinner.selectedItem.toString(),
                "direccion" to direccionText.text.toString(),
                "fechaNac" to fechaNac.text.toString(),
                "telefono" to telefonoPhone.text.toString()
            )

            db.collection("usuarios")
                .document(email.toString())
                .set(datos)
                .addOnSuccessListener {
                    alert("Exito!", "Regsitro existoso")
                }
                .addOnFailureListener {
                    alert("Error!", "Las contraseñas no son iguales")
                }

        }

    }

    private fun alert(title: String, message: String) {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}