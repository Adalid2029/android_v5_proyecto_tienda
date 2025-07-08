package com.version5.tiendavirtual.productos.modelos

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val imagenUrl: String,
    val descripcion: String,
    val disponible: Boolean,
    val cantidad: Int
)