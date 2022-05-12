package com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_p2_bdfirestore_edgaralbertovalderramacastillo.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.insertar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()

            val datos = hashMapOf(
                "descripcion" to binding.descripcion.text.toString(),
                "division" to binding.division.text.toString(),
                "cantidad_empleados" to binding.cEmp.text.toString().toInt()
            )

            baseRemota.collection("area")
                .add(datos)
                .addOnSuccessListener {
                    //SI SE PUDO!
                    Toast.makeText(activity,"SE INSERTÓ CON EXITO!", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                        AlertDialog.Builder(requireContext())
                            .setMessage(it.message)
                            .show()
                }

            binding.descripcion.setText("")
            binding.division.setText("")
            binding.cEmp.setText("")

        }

        binding.insertarSub.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()

            val datos = hashMapOf(
                "IDEDIFICIO" to binding.idEdificio.text.toString(),
                "PISO" to binding.piso.text.toString(),
            )

            baseRemota.collection("subdepartamento")
                .add(datos)
                .addOnSuccessListener {
                    //SI SE PUDO!
                    Toast.makeText(activity,"SE INSERTÓ CON EXITO!", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }

            binding.idEdificio.setText("")
            binding.piso.setText("")
        }

        homeViewModel.text.observe(viewLifecycleOwner) {
        }
        return root


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}