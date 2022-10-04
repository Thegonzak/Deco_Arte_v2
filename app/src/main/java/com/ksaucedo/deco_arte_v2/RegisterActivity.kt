package com.ksaucedo.deco_arte_v2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.ksaucedo.deco_arte_v2.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var email: String? = null
    private var provider: String? = null
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var arrayCiudad: ArrayAdapter<String>


    //Alertas
    //conexion a base de datos
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        email = bundle?.getString("email")

        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayCiudad = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.btnGuardar.setOnClickListener {
            Guardar()
        }

        /**
         * Cargar ciudades de la base de datos firebase
         */
        db.collection("ciudad").get().addOnSuccessListener { result ->
            for (document in result) {
                arrayCiudad.add(document.get("nombre").toString())
            }

            binding.ciudadSpinner.adapter = arrayCiudad
        }

    }

    //validar segunda clave
    private fun Guardar() {
        if (binding.cedulaText.text.isNotBlank() &&
            binding.nombreText.text.isNotBlank() &&
            binding.apellidosText.text.isNotBlank() &&
            binding.direccionText.text.isNotBlank() &&
            binding.fechaNac.text.isNotBlank() &&
            binding.telefonoPhone.text.isNotBlank()
        ) {
            val datos = hashMapOf(
                "cedula" to binding.cedulaText.text.toString(),
                "nombre" to binding.nombreText.text.toString(),
                "apellido" to binding.apellidosText.text.toString(),
                "ciudad" to binding.ciudadSpinner.selectedItem.toString(),
                "direccion" to binding.direccionText.text.toString(),
                "fechaNac" to binding.fechaNac.text.toString(),
                "telefono" to binding.telefonoPhone.text.toString()
            )

            db.collection("usuarios")
                .document(email.toString())
                .set(datos)
                .addOnSuccessListener {
                    alert("Exito!", "Regsitro existoso")
                    val prefs =
                        getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
                    prefs.putBoolean("userData", true)
                    prefs.apply()
                    showHome(email ?: "", ProviderType.valueOf(provider ?: ""))
                }
                .addOnFailureListener {
                    alert("Error!", "Las contraseñas no son iguales")
                }

        }

    }

    private fun alert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}