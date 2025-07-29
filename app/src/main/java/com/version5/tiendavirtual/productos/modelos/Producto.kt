package com.version5.tiendavirtual.productos.modelos

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val imagenUrl: String?,
    val descripcion: String,
    val disponible: Boolean,
    val cantidad: Int
)

data class ProductoApi(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: String,
    val cantidad_stock: String,
    val categoria_id: String,
    val imagen_url: String?,
    val disponible: String,
    val categoria_nombre: String,
    val categoria_color: String?
)

fun ProductoApi.aProducto(): Producto {
    return Producto(
        id = this.id.toInt(),
        nombre = this.nombre,
        categoria = this.categoria_nombre,
        precio = this.precio.toDouble(),
        imagenUrl = this.imagen_url,
        descripcion = this.descripcion,
        disponible = this.disponible == "1",
        cantidad = this.cantidad_stock.toInt()
    )
}