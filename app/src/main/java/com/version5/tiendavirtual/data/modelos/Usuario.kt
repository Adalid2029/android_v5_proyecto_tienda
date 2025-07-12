package com.version5.tiendavirtual.data.modelos

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val nombreUsuario: String,

    @SerializedName("email")
    val email: Int,

    @SerializedName("nombre_completo")
    val nombreCompleto: Int,

    @SerializedName("telefono")
    val telefono: Int,

    @SerializedName("groups")
    val grupos: Int,

    @SerializedName("permissions")
    val permisos: Int
)