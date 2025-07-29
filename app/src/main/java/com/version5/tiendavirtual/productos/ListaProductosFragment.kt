package com.version5.tiendavirtual.productos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.version5.tiendavirtual.R
import com.version5.tiendavirtual.data.red.RetrofitCliente
import com.version5.tiendavirtual.productos.modelos.Producto
import com.version5.tiendavirtual.productos.modelos.aProducto
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListaProductosFragment : Fragment() {
    private lateinit var rvProductos: RecyclerView
    private lateinit var btnAnadirNuevoProducto: Button
    private lateinit var etBuscarProducto: EditText

    private lateinit var adaptadorProductos: AdaptadorProductos
    private var listaProductos = mutableListOf<Producto>()

    private var trabajoBusqueda: Job? = null
    private var textoBusquedaActual = ""

    private var paginaActual = 1
    private var totalResultados = 0
    private var estaCargando = false
    private var hayMasPaginas = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_lista_productos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarVistas(view)
        configurarRecyclerView()
        configurarListeners()
        // cargarProductoEjemplo()
        cargarProductosDesdeApi()
    }

    private fun configurarListeners() {
        btnAnadirNuevoProducto.setOnClickListener {
            Toast.makeText(requireContext(), "Añadir nuevo producto", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_lista_productos_to_anadir_producto)
        }
        etBuscarProducto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                val textoBusqueda = s.toString().trim()

                trabajoBusqueda?.cancel()

                trabajoBusqueda = lifecycleScope.launch {
                    delay(500)
                    buscarProductos(textoBusqueda)
                }
            }

        })
    }

    private fun configurarRecyclerView() {
        adaptadorProductos = AdaptadorProductos(
            listaProductos = listaProductos,
            alHacerClickEnComprar = { producto ->
                Toast.makeText(
                    requireContext(),
                    "Adicionado al carrito: ${producto.nombre}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        val layoutManager = LinearLayoutManager(requireContext())
        rvProductos.apply {
            this.layoutManager = layoutManager
            adapter = adaptadorProductos

            // Agregar el listener para detectar el scroll hacia abajo
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // Verificar si se desplaza hacia abajo
                    if (dy > 0) {
                        val itemsVisibles = layoutManager.childCount
                        val totalItems = layoutManager.itemCount
                        val primerItemVisible = layoutManager.findFirstVisibleItemPosition()

                        // Verificar si estamos cerca del final (es decit los 5 ultimos productos)
                        if (!estaCargando && hayMasPaginas && (itemsVisibles + primerItemVisible) >= totalItems - 5) {
                            cargarSiguientePagina()
                        }
                    }
                }
            })
        }
    }

    private fun cargarSiguientePagina() {
        if (!hayMasPaginas || estaCargando) return
        paginaActual++
        buscarProductos(textoBusquedaActual)
    }

    private fun buscarProductos(textoBusqueda: String) {
        if (textoBusquedaActual != textoBusqueda) {
            paginaActual = 1
            hayMasPaginas = true
            listaProductos.clear()
        }
        textoBusquedaActual = textoBusqueda

        if (estaCargando) return

        estaCargando = true

        lifecycleScope.launch {
            try {
                val respuesta = if (textoBusqueda.isEmpty()) {
                    RetrofitCliente.apiServicio.obtenerProductos(pagina = paginaActual)
                } else {
                    RetrofitCliente.apiServicio.obtenerProductos(
                        paginaActual,
                        busqueda = textoBusqueda
                    )
                }
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val datos = respuesta.body()!!.datos
                    val productosApi = datos.productos
                    val productos = productosApi.map { it.aProducto() }

                    totalResultados = datos.paginacion.total_resultados
                    hayMasPaginas = productos.size == datos.paginacion.limite

                    listaProductos.addAll(productos)
                    adaptadorProductos.notificarCambios()

                    if (listaProductos.isEmpty() && textoBusqueda.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "No se encontraron productos para: ${textoBusqueda}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al buscar productos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error de conexion: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                estaCargando = false
            }

        }
    }

    fun inicializarVistas(view: View) {
        etBuscarProducto = view.findViewById(R.id.et_buscar_productos)
        btnAnadirNuevoProducto = view.findViewById(R.id.btn_anadir_nuevo_producto)
        rvProductos = view.findViewById(R.id.rv_productos)
    }

    fun cargarProductosDesdeApi() {
        paginaActual = 1
        hayMasPaginas = true
        buscarProductos("")
    }

    private fun cargarProductoEjemplo() {
        val productosEjemplo = listOf(
            Producto(
                id = 1,
                nombre = "Arroz",
                categoria = "Abarrotes",
                precio = 4.5,
                imagenUrl = "",
                descripcion = "Producto que es necesario para el hogar",
                disponible = true,
                cantidad = 1
            ),
            Producto(
                id = 1,
                nombre = "Azucar",
                categoria = "Abarrotes",
                precio = 4.5,
                imagenUrl = "",
                descripcion = "Producto que es necesario para el hogar",
                disponible = true,
                cantidad = 1
            ),
            Producto(
                id = 1,
                nombre = "Harina",
                categoria = "Abarrotes",
                precio = 4.5,
                imagenUrl = "",
                descripcion = "Producto que es necesario para el hogar",
                disponible = true,
                cantidad = 1
            ),
            Producto(
                id = 1,
                nombre = "Trigo",
                categoria = "Abarrotes",
                precio = 4.5,
                imagenUrl = "",
                descripcion = "Producto que es necesario para el hogar",
                disponible = true,
                cantidad = 1
            )
        )
        listaProductos.clear()
        listaProductos.addAll(productosEjemplo)
        adaptadorProductos.notificarCambios()
    }
}