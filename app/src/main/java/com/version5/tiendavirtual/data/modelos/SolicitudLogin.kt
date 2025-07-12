package com.version5.tiendavirtual.data.modelos

import com.google.gson.annotations.SerializedName

data class SolicitudLogin (
    @SerializedName("login")
    val login: String,

    @SerializedName("password")
    val contrasena: String,
)