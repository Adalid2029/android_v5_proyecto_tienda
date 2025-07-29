package com.version5.tiendavirtual.data.modelos

import com.version5.tiendavirtual.productos.modelos.ProductoApi

data class RespuestaProductos(
    val estado: String,
    val mensaje: String,
    val datos: DatosProductos,
    val timestamp: String
)

data class DatosProductos(
    val productos: List<ProductoApi>,
    val paginacion: Paginacion
)

data class Paginacion(
    val pagina_actual: Int,
    val limite: Int,
    val total_resultados: Int
)