package com.version5.tiendavirtual.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.version5.tiendavirtual.R
import com.version5.tiendavirtual.data.almacenamiento.TokenManager
import kotlinx.coroutines.launch

class PerfilFragment : Fragment() {
    private lateinit var btnCerrarSesion: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarVistas(view)
        configurarListeners()
    }

    private fun configurarListeners() {
        btnCerrarSesion.setOnClickListener {
            lifecycleScope.launch {
                val tokenManager = TokenManager(requireContext())
                tokenManager.limpiarSesion()
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    private fun inicializarVistas(view: android.view.View) {
        btnCerrarSesion = view.findViewById<Button>(R.id.btn_cerrar_sesion)
    }
}