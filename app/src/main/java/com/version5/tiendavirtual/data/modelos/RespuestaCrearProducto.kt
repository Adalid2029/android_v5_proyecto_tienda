package com.version5.tiendavirtual.data.modelos

data class RespuestaCrearProducto(
    val estado: String,
    val mensaje: String,
    val datos: ProductoCreado?,
    val errores: Map<String, String>,
    val timestamp: String
)

data class ProductoCreado(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: String
)