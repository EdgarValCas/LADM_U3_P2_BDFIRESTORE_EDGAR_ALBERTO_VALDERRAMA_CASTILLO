package com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.databinding.ActivityMain2Binding
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {
    var idElegido = ""
    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        idElegido = intent.extras!!.getString("idelegido")!!


        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("area")
            .document(idElegido)
            .get()
            .addOnSuccessListener {
                binding.descripcion.setText(it.getString("descripcion"))
                binding.division.setText(it.getString("division"))
                binding.cemp.setText(it.getString("cantidad_empleados").toString())
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .show()
            }

        binding.regresar.setOnClickListener {
            finish()
        }

        binding.actualizar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("area")
                .document(idElegido)
                .update("descripcion" , binding.descripcion.text.toString(),
                            "division", binding.division.text.toString(),
                                "cantidad_empleados",binding.cemp.text.toString().toInt())
                .addOnSuccessListener {
                    Toast.makeText(this,"SE ACTUALIZÓ CORRECTAMENTE!",Toast.LENGTH_LONG)
                        .show()
                    binding.descripcion.text.clear()
                    binding.division.text.clear()
                    binding.cemp.text.clear()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}