package com.version5.tiendavirtual.data.red

import com.version5.tiendavirtual.data.modelos.DatosLogin
import com.version5.tiendavirtual.data.modelos.RespuestaApi
import com.version5.tiendavirtual.data.modelos.SolicitudLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServicio {
    @POST("auth/login")
    suspend fun iniciarSesion(
        @Body solicitud: SolicitudLogin
    ): Response<RespuestaApi<DatosLogin>>
}