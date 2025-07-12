package com.version5.tiendavirtual.data.modelos

import com.google.gson.annotations.SerializedName

class DatosLogin(
    @SerializedName("token")
    val token: String,

    @SerializedName("user")
    val usuario: Usuario,

    @SerializedName("expires_at")
    val expiraEn: String
)
