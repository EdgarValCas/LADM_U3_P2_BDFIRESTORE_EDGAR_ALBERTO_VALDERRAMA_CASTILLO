package com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.ui.dashboard

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
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.databinding.FragmentDashboardBinding
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.ui.notifications.MainActivity2
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.ui.notifications.NotificationsFragment
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val arreglo1 = ArrayList<String>()
    val idlista1 = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //EVENTO (SE DISPARA SOLITO)
        FirebaseFirestore.getInstance()
            .collection("area")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //SI HUBO ERROR
                        AlertDialog.Builder(requireContext())
                            .setMessage(error.message)
                            .show()
                    return@addSnapshotListener
                }

                arreglo1.clear()
                idlista1.clear()
                for (documento in query!!) {
                    var cadena = "descripcion: ${documento.getString("descripcion")}\n" +
                                    "division: ${documento.getString("division")}\n"+
                                    "cantidad de empleados: ${documento.getLong("cantidad_empleados")} "
                    arreglo1.add(cadena)

                    idlista1.add(documento.id.toString())
                }

                binding.lista.adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, arreglo1)
                binding.lista.setOnItemClickListener { adapterView, view, posicion, l ->
                    dialogoEliminaActualiza(posicion)
                }
            }

        dashboardViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    private fun dialogoEliminaActualiza(posicion: Int) {
        var idElegido = idlista1.get(posicion)

        AlertDialog.Builder(requireContext()).setTitle("ATENCIÓN")
            .setMessage("QUE DESEAS HACER CON\n${arreglo1.get(posicion)}?")
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
            .collection("area")
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