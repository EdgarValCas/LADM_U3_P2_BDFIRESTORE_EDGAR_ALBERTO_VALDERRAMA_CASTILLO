package com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.databinding.ActivityMain2Binding
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.databinding.ActivityMain3Binding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity3 : AppCompatActivity() {
    var idElegido = ""
    lateinit var binding: ActivityMain3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        idElegido = intent.extras!!.getString("idelegido")!!


        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("subdepartamento")
            .document(idElegido)
            .get()
            .addOnSuccessListener {
                binding.idEdificio.setText(it.getString("IDEDIFICIO"))
                binding.piso.setText(it.getString("PISO"))
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .show()
            }

        binding.regresar.setOnClickListener {
            finish()
        }

        binding.actualizarSub.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("subdepartamento")
                .document(idElegido)
                .update("IDEDIFICIO" , binding.idEdificio.text.toString(),
                    "PISO", binding.piso.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"SE ACTUALIZÓ CORRECTAMENTE!", Toast.LENGTH_LONG)
                        .show()
                    binding.idEdificio.text.clear()
                    binding.piso.text.clear()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}