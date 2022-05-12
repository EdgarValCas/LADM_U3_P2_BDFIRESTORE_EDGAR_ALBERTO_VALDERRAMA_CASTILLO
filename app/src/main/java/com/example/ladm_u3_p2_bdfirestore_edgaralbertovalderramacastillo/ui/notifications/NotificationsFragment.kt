package com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.ui.notifications

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.databinding.FragmentNotificationsBinding
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val arreglo2 = ArrayList<String>()
    val idlista2 = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //EVENTO (SE DISPARA SOLITO)
        FirebaseFirestore.getInstance()
            .collection("subdepartamento")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //SI HUBO ERROR
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                arreglo2.clear()
                idlista2.clear()
                for (documento in query!!) {
                    var cadena = "ID Edificio: ${documento.getString("IDEDIFICIO")}\n" +
                            "Piso: ${documento.getString("PISO")}"
                    arreglo2.add(cadena)

                    idlista2.add(documento.id.toString())
                }

                binding.listaSub.adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, arreglo2)
                binding.listaSub.setOnItemClickListener { adapterView, view, posicion, l ->
                    dialogoEliminaActualiza(posicion)
                }
            }

        notificationsViewModel.text.observe(viewLifecycleOwner) {
        }
        return root
    }

    private fun dialogoEliminaActualiza(posicion: Int) {
        var idElegido = idlista2.get(posicion)

        AlertDialog.Builder(requireContext()).setTitle("ATENCIÓN")
            .setMessage("QUE DESEAS HACER CON\n${arreglo2.get(posicion)}?")
            .setPositiveButton("ELIMINAR"){d,i->
                eliminar(idElegido)
            }
            .setNeutralButton("ACTUALIZAR"){d,i->
                actualizar(idElegido)
            }
            .setNegativeButton("CANCELAR"){d,i,->}
            .show()
    }

    private fun actualizar(idElegido: String) {
        var otraVentana = Intent(requireContext(),MainActivity2::class.java)

        otraVentana.putExtra("idelegido",idElegido)

        startActivity(otraVentana)
    }

    private fun eliminar(idElegido: String) {
        FirebaseFirestore.getInstance()
            .collection("subdepartamento")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"SE ELIMINÓ CON EXITO!", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(it.message)
                    .show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}