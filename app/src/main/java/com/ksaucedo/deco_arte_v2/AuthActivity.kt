package com.ksaucedo.deco_arte_v2


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

/* agregar la extensiones de kotlin para poder llamar las id de las clases xml a JAVA. */

class AuthActivity : AppCompatActivity() {

    private var userData: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        //splash
        Thread.sleep(2000)
        setTheme(R.style.Theme_Deco_Arte_v2)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Cambiar color de la barra de estado
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.naranja)

        // Setup
        setup()
        session()

    }

    private fun session() {
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)
        val email = prefs.getString("email", null)
        userData = prefs.getBoolean("userData", false)

        if (email != null) {
            authLayout.visibility = View.INVISIBLE
            showHome(email)
        }
    }

    private fun setup() {// accion para registrarse
        registrarseButton.setOnClickListener {
            if (emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        emailText.text.toString(),
                        passwordText.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {//En caso de que este correcto muestra el siguiente metodo
                            showHome(it.result?.user?.email ?: "")
                        } else {// En caso de mostrar error se llama el metodo alertas.
                            alertas()
                        }
                    }
            }

        }//accion para iniciar session
        iniciarSessionButton.setOnClickListener {
            if (emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        emailText.text.toString(),
                        passwordText.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {//En caso de que este correcto muestra el siguiente metodo
                            showHome(it.result?.user?.email ?: "")
                        } else {// En caso de mostrar error se llama el metodo alertas.
                            alertas()
                        }
                    }
            }
        }

    }

    // Metodo para detectar algun error, muestra un mensaje y un boton.
    private fun alertas() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }
}