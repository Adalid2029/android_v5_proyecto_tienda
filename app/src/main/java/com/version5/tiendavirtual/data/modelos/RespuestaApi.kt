package com.version5.tiendavirtual.data.modelos

import com.google.gson.annotations.SerializedName

data class RespuestaApi<T>(
    @SerializedName("estado")
    val estado: String,

    @SerializedName("mensaje")
    val mensaje: String,

    @SerializedName("datos")
    val datos: T? = null,

    @SerializedName("errores")
    val errores: Map<String, String>? = null,

    @SerializedName("timestamp")
    val marcaTiempo: String
) {
    /*
    * Verificar si la respuesta es exitosa
     */
    fun esExitosa(): Boolean = estado == "exito"

    /*
    * Verifica si hay algun error en la respuesta
     */

    fun tieneErrores(): Boolean = errores != null && errores.isNotEmpty()
}