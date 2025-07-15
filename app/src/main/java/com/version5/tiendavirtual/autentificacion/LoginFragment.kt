package com.version5.tiendavirtual.autentificacion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.version5.tiendavirtual.R
import com.version5.tiendavirtual.data.almacenamiento.TokenManager
import com.version5.tiendavirtual.data.modelos.DatosLogin
import com.version5.tiendavirtual.data.modelos.SolicitudLogin
import com.version5.tiendavirtual.data.red.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class LoginFragment : Fragment() {
    private lateinit var etNombreUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var tvRecuperarPassword: TextView

    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager(requireContext())
        inicializarVistas(view)
        configurarListeners()
        verificarSesionExistente()
    }

    private fun verificarSesionExistente() {
        lifecycleScope.launch {
            tokenManager.estaLogueado().collect { estaLogueado ->
                if (estaLogueado) {
                    navegarAPrincipal()
                }
            }
        }
    }

    private fun navegarAPrincipal() {
        try {
            findNavController().navigate(R.id.action_login_to_lista_productos)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error al redireccionar a Principal",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun inicializarVistas(view: View) {
        etNombreUsuario = view.findViewById(R.id.et_nombre_usuario)
        etContrasena = view.findViewById(R.id.et_contrasena)
        btnIniciarSesion = view.findViewById(R.id.btn_iniciar_sesion)
        tvRecuperarPassword = view.findViewById(R.id.tv_recuperar_contrasena)
    }

    private fun configurarListeners() {
        btnIniciarSesion.setOnClickListener() {
            // iniciarSesion()
            iniciarSesionConApi()
        }
    }

    private fun iniciarSesionConApi() {
        val usuario = etNombreUsuario.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        if (!validarCampos(usuario, contrasena)) {
            return
        }
        lifecycleScope.launch {
            try {
                val solicitudLogin = SolicitudLogin(
                    login = usuario,
                    contrasena = contrasena
                )

                val respuesta = RetrofitCliente.apiServicio.iniciarSesion(solicitudLogin)

                if (respuesta.isSuccessful) {
                    val cuerpoRespuesta = respuesta.body()
                    if (cuerpoRespuesta != null && cuerpoRespuesta.esExitosa()) {
                        manejarLoginExitoso(cuerpoRespuesta)
                    } else {
                        manejarErrorApi(cuerpoRespuesta)
                    }
                }
            } catch (e: IOException) {
                manejarErrorRed(e)
            } catch (e: HttpException) {
                manejarErrorHttp(e.code())
            } catch (e: Exception) {
                manejarErrorDeconocido(e)
            }
        }
    }

    private fun manejarErrorDeconocido(error: Exception) {
        mostrarMensaje("Error inesperado: ${error.message}")
    }

    private fun manejarErrorHttp(codigoError: Int) {
        val mensaje = when (codigoError) {
            401 -> "Credenciales incorrectas"
            404 -> "Servicio no encontrado"
            500 -> "Error interno del servidor"
            503 -> "Servicio no disponible"
            else -> "Error de conexión ${codigoError}"
        }
        mostrarMensaje(mensaje)
    }

    private fun manejarErrorRed(error: Exception) {
        val mensaje = when {
            error.message?.contains("timeout") == true -> "Tiempo de espera agotado"
            error.message?.contains("network") == true -> "Sin conexión a internet"
            else -> "Error de conexión. Verifica tu internet"
        }
        mostrarMensaje(mensaje)
    }

    private fun manejarErrorApi(respuesta: com.version5.tiendavirtual.data.modelos.RespuestaApi<com.version5.tiendavirtual.data.modelos.DatosLogin>?) {
        Log.d("ApiLogin", respuesta.toString())
        when {
            respuesta == null -> {
                mostrarMensaje("Error desconocido del servidor")
            }

            respuesta.tieneErrores() -> {
                manejarErroresValidacion(respuesta)
            }

            else -> {
                mostrarMensaje(respuesta.mensaje)
            }
        }
    }

    private fun manejarErroresValidacion(respuesta: com.version5.tiendavirtual.data.modelos.RespuestaApi<com.version5.tiendavirtual.data.modelos.DatosLogin>) {
        etNombreUsuario.error = null
        etContrasena.error = null

        respuesta.errores?.let { errores ->
            errores["login"]?.let { error ->
                etNombreUsuario.error = error
            }
            errores["password"]?.let { error ->
                etContrasena.error = error
            }
            if (errores.isNotEmpty() && etNombreUsuario.error == null && etContrasena.error == null) {
                mostrarMensaje(errores.values.first())
            }
        }
    }

    private suspend fun manejarLoginExitoso(respuesta: com.version5.tiendavirtual.data.modelos.RespuestaApi<com.version5.tiendavirtual.data.modelos.DatosLogin>) {
        val datosLogin = respuesta.datos
        if (datosLogin != null) {
            tokenManager.guardarDatosSesion(
                token = datosLogin.token,
                usuarioId = datosLogin.usuario.id.toString(),
                nombreUsuario = datosLogin.usuario.nombreUsuario,
                email = datosLogin.usuario.email,
                nombreCompleto = datosLogin.usuario.nombreCompleto,
                expiraEn = datosLogin.expiraEn
            )
            mostrarMensaje("¡Bienvenido ${datosLogin.usuario.nombreCompleto}")
            navegarAPrincipal()
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
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
            Toast.makeText(
                requireContext(),
                "¡Bienvenidos a la tienda virtual!",
                Toast.LENGTH_SHORT
            )
                .show()
            findNavController().navigate(R.id.action_login_to_lista_productos)
        } else {
            Toast.makeText(
                requireContext(),
                "Usuario o contraseña incorrectas",
                Toast.LENGTH_SHORT
            )
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