package com.version5.tiendavirtual.autentificacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.version5.tiendavirtual.R

class LoginFragment : Fragment() {
    private lateinit var etNombreUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var tvRecuperarPassword: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarVistas(view)
        configurarListeners()
    }

    private fun inicializarVistas(view: View) {
        etNombreUsuario = view.findViewById(R.id.et_nombre_usuario)
        etContrasena = view.findViewById(R.id.et_contrasena)
        btnIniciarSesion = view.findViewById(R.id.btn_iniciar_sesion)
        tvRecuperarPassword = view.findViewById(R.id.tv_recuperar_contrasena)
    }

    private fun configurarListeners() {
        btnIniciarSesion.setOnClickListener() {
            iniciarSesion()
        }
    }

    private fun iniciarSesion() {
        val usuario = etNombreUsuario.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        if (!validarCampos(usuario, contrasena)) {
            return
        }
        // simular el inicio de sesion
        if (validarCredenciales(usuario, contrasena)) {
            // refireccionar al la lista de los productos
            Toast.makeText(requireContext(), "¡Bienvenidos a la tienda virtual!", Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_login_to_lista_productos)
        } else {
            Toast.makeText(requireContext(), "Usuario o contraseña incorrectas", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun validarCampos(usuario: String, contrasena: String): Boolean {
        var esValido = true

        if (usuario.isEmpty()) {
            etNombreUsuario.error = "Ingresa tu nombre de usuario"
            esValido = false
        } else {
            etNombreUsuario.error = null
        }

        if (contrasena.isEmpty()) {
            etContrasena.error = "Ingresa tu contraseña"
            esValido = false
        } else {
            etContrasena.error = null
        }
        return esValido
    }

    private fun validarCredenciales(usuario: String, contrasena: String): Boolean {
        return usuario == "admin" && contrasena == "123456"
    }
}