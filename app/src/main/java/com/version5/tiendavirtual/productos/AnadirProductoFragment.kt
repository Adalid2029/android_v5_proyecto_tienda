package com.version5.tiendavirtual.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.version5.tiendavirtual.R
import com.version5.tiendavirtual.data.almacenamiento.TokenManager
import com.version5.tiendavirtual.data.modelos.SolicitudCrearProducto
import com.version5.tiendavirtual.data.red.RetrofitCliente
import kotlinx.coroutines.launch

class AnadirProductoFragment : Fragment() {
    private lateinit var etNombreProducto: EditText
    private lateinit var etPrecioProducto: EditText
    private lateinit var etCantidadProducto: EditText
    private lateinit var etDescripcionProducto: EditText
    private lateinit var btnAnadirProducto: Button

    private var tokenActual: String? = null

    private lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_anadir_producto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniciarlizarGestores()
        lifecycleScope.launch {
            tokenManager.obtenerToken().collect { token ->
                tokenActual = token
            }
        }
        inicializarVistas(view)
        configurarListeners()
    }

    private fun configurarListeners() {
        btnAnadirProducto.setOnClickListener {
            if (validarCampos()) {
                crearProducto()
            }
        }
    }

    private fun crearProducto() {
        lifecycleScope.launch {
            try {
                val token = tokenManager.obtenerTokenSincrono()
                val solicitud = SolicitudCrearProducto(
                    nombre = etNombreProducto.text.toString().trim(),
                    descripcion = etDescripcionProducto.text.toString().trim(),
                    precio = etPrecioProducto.text.toString().toDouble(),
                    cantidad_stock = etCantidadProducto.text.toString().toInt(),
                    categoria_id = 1,
                )
                val respuesta =
                    RetrofitCliente.apiServicio.crearProducto(token.toString(), solicitud)
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val productoCreado = respuesta.body()!!
                    if (productoCreado.estado == "exito") {
                        Toast.makeText(requireContext(), "Producto creado", Toast.LENGTH_SHORT)
                            .show()
                        regresarAListaProductos()

                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al crear producto ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun regresarAListaProductos() {
        findNavController().navigateUp()
    }

    private fun inicializarVistas(view: View) {
        etNombreProducto = view.findViewById<EditText>(R.id.et_nombre_producto)
        etPrecioProducto = view.findViewById<EditText>(R.id.et_precio_producto)
        etCantidadProducto = view.findViewById<EditText>(R.id.et_cantidad_producto)
        etDescripcionProducto = view.findViewById<EditText>(R.id.et_descripcion_producto)
        btnAnadirProducto = view.findViewById<Button>(R.id.btn_anadir_producto)
    }

    private fun iniciarlizarGestores() {
        tokenManager = TokenManager(requireContext())
    }

    private fun validarCampos(): Boolean {
        val nombre = etNombreProducto.text.toString().trim()
        val precio = etPrecioProducto.text.toString().trim()
        val cantidad = etCantidadProducto.text.toString().trim()
        val descripcion = etDescripcionProducto.text.toString().trim()

        etNombreProducto.error = null
        etPrecioProducto.error = null
        etCantidadProducto.error = null
        etDescripcionProducto.error = null

        var esValido = true

        when {
            nombre.isEmpty() -> {
                etNombreProducto.error = "El nombre es obligatorio"
                esValido = false
            }

            nombre.length < 3 -> {
                etNombreProducto.error = "El nombre debe tener al menos 3 caracteres"
                esValido = false
            }
        }

        when {
            precio.isEmpty() -> {
                etPrecioProducto.error = "El precio es obligatorio"
                esValido = false
            }

            precio.toDoubleOrNull() == null -> {
                etPrecioProducto.error = "El precio debe ser un numero valido"
                esValido = false
            }

            precio.toDouble() <= 0 -> {
                etPrecioProducto.error = "El precio debe ser mayor a 0"
                esValido = false
            }
        }

        when {
            cantidad.isEmpty() -> {
                etCantidadProducto.error = "La cantidad es obligatoria"
                esValido = false
            }

            cantidad.toIntOrNull() == null -> {
                etCantidadProducto.error = "La cantidad debe ser un numero valido"
                esValido = false
            }

            cantidad.toInt() <= 0 -> {
                etCantidadProducto.error = "La cantidad debe ser mayor a 0"
                esValido = false
            }
        }

        when {
            descripcion.isEmpty() -> {
                etDescripcionProducto.error = "La descripcion es obligatoria"
                esValido = false
            }

            descripcion.length < 10 -> {
                etDescripcionProducto.error =
                    "La descripcion debe tener al menos 10 caracteres (tienes ${descripcion.length}"
                esValido = false
            }
        }
        return esValido
    }
}


