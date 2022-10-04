package com.ksaucedo.deco_arte_v2

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.ksaucedo.deco_arte_v2.model.Novedad
import java.text.SimpleDateFormat
import java.util.*

class NuevaNovedad : Fragment(R.layout.fragment_nueva_novedad) {

    private var myFormat: String = "MM/dd/yy"
    private val dateFormat: SimpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
    private val myCalendar: Calendar = Calendar.getInstance()
    private lateinit var fechaInicio: EditText
    private lateinit var fechaFin: EditText
    private lateinit var texto: EditText
    private lateinit var actvivo: CheckBox
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var id: String = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        texto = view.findViewById(R.id.texto)
        fechaInicio = view.findViewById(R.id.fechaInicio)
        fechaFin = view.findViewById(R.id.fechaFin)
        actvivo = view.findViewById(R.id.actvivo)

        if (id != "") {
            db.collection("novedades").document(id).get().addOnSuccessListener {
                if (it.exists()) {
                    texto.setText(it.get("texto").toString())
                    actvivo.isChecked = it.getBoolean("activo")!!
                    fechaInicio.setText(
                        dateFormat.format(
                            it.getTimestamp("fecha_inicio")?.toDate()?.time ?: 0
                        )
                    )
                    fechaFin.setText(
                        dateFormat.format(
                            it.getTimestamp("fecha_fin")?.toDate()?.time ?: 0
                        )
                    )
                }
            }
        }

        view.findViewById<Button>(R.id.btnRegresar).setOnClickListener {
            (activity as HomeActivity).goToHome()
        }

        view.findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            guardaNovedad()
        }
        val dateInicio = OnDateSetListener { _, year, month, day ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            updateFechaInicio()
        }
        val dateFin = OnDateSetListener { _, year, month, day ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            updateFechaFin()
        }

        fechaInicio.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                dateInicio,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        fechaFin.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                dateFin,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    private fun updateFechaInicio() {
        fechaInicio.setText(dateFormat.format(myCalendar.time))
    }

    private fun updateFechaFin() {
        fechaFin.setText(dateFormat.format(myCalendar.time))
    }

    private fun guardaNovedad() {
        val dateFechaInicio = SimpleDateFormat(myFormat).parse(fechaInicio.text.toString())
        val dateFechaFin = SimpleDateFormat(myFormat).parse(fechaFin.text.toString())
        val datos = hashMapOf(
            "texto" to texto.text.toString(),
            "fecha_inicio" to dateFechaInicio?.let { Timestamp(it) },
            "fecha_fin" to dateFechaFin?.let { Timestamp(it) },
            "activo" to actvivo.isChecked,
        )

        if (id != "") {
            db.collection("novedades")
                .document(id)
                .set(datos)
                .addOnSuccessListener {
                    alert("Exito!", "Regsitro existoso")
                    clear()
                    (activity as HomeActivity).goToHome()
                }
                .addOnFailureListener {
                    alert("Error!", "Ha ocurrido un error")
                    clear()
                    (activity as HomeActivity).goToHome()
                }
        } else {
            db.collection("novedades")
                .add(datos)
                .addOnSuccessListener {
                    alert("Exito!", "Regsitro existoso")
                    clear()
                    (activity as HomeActivity).goToHome()
                }
                .addOnFailureListener {
                    alert("Error!", "Ha ocurrido un error")
                    clear()
                    (activity as HomeActivity).goToHome()
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

    private fun clear() {
        id = ""
        texto.setText("")
        fechaInicio.setText("")
        fechaFin.setText("")
        actvivo.isChecked = false
    }
}