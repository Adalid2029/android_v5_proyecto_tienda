package com.version5.tiendavirtual.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.version5.tiendavirtual.productos.modelos.Producto
import com.version5.tiendavirtual.R

class AdaptadorProductos(
    private val listaProductos: List<Producto>,
    private val alHacerClickEnComprar: (Producto) -> Unit
) : RecyclerView.Adapter<AdaptadorProductos.ViewHolderProducto>() {
    class ViewHolderProducto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagenProducto: ImageView = itemView.findViewById(R.id.iv_imagen_producto)
        val tvCategoria: TextView = itemView.findViewById(R.id.tv_categoria)
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tv_nombre_producto)
        val tvPrecio: TextView = itemView.findViewById(R.id.tv_precio)
        val btnComprar: Button = itemView.findViewById(R.id.btn_comprar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderProducto {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_productos, parent, false)
        return ViewHolderProducto(vista)
    }

    override fun onBindViewHolder(
        holder: ViewHolderProducto,
        position: Int
    ) {
        val producto = listaProductos[position]
        holder.tvCategoria.text = "Categoria: ${producto.categoria}"
        holder.tvNombreProducto.text = producto.nombre
        holder.tvPrecio.text = "Precio: ${producto.precio} Bs"

        holder.ivImagenProducto.setImageResource(R.drawable.store_icon)
        holder.btnComprar.setOnClickListener {
            alHacerClickEnComprar(producto)
        }
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }
    fun notificarCambios(){
        notifyDataSetChanged()
    }
}