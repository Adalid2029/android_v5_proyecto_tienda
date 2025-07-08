package com.version5.tiendavirtual.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.version5.tiendavirtual.R
import com.version5.tiendavirtual.productos.modelos.Producto

class ListaProductosFragment : Fragment() {
    private lateinit var rvProductos: RecyclerView
    private lateinit var btnAnadirNuevoProducto: Button
    private lateinit var etBuscarProducto: EditText

    private lateinit var adaptadorProductos: AdaptadorProductos
    private var listaProductos = mutableListOf<Producto>()
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
        cargarProductoEjemplo()
    }

    private fun configurarListeners() {
        btnAnadirNuevoProducto.setOnClickListener {
            Toast.makeText(requireContext(), "Añadir nuevo producto", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_lista_productos_to_anadir_producto)
        }
    }

    private fun configurarRecyclerView() {
        adaptadorProductos = AdaptadorProductos(
            listaProductos = listaProductos,
            alHacerClickEnComprar = { producto ->
                Toast.makeText(requireContext(), "Adicionado al carrito: ${producto.nombre}", Toast.LENGTH_SHORT).show()
            }
        )
        rvProductos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adaptadorProductos
        }
    }

    fun inicializarVistas(view: View) {
        etBuscarProducto = view.findViewById(R.id.et_buscar_productos)
        btnAnadirNuevoProducto = view.findViewById(R.id.btn_anadir_nuevo_producto)
        rvProductos = view.findViewById(R.id.rv_productos)
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