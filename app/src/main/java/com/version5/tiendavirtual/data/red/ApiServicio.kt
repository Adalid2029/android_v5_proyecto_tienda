package com.version5.tiendavirtual.data.red

import com.version5.tiendavirtual.data.modelos.DatosLogin
import com.version5.tiendavirtual.data.modelos.RespuestaApi
import com.version5.tiendavirtual.data.modelos.RespuestaCrearProducto
import com.version5.tiendavirtual.data.modelos.RespuestaProductos
import com.version5.tiendavirtual.data.modelos.SolicitudCrearProducto
import com.version5.tiendavirtual.data.modelos.SolicitudLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServicio {
    @POST("auth/login")
    suspend fun iniciarSesion(
        @Body solicitud: SolicitudLogin
    ): Response<RespuestaApi<DatosLogin>>

    @GET("productos")
    suspend fun obtenerProductos(
        @Query("pagina") pagina: Int? = 1,
        @Query("limite") limite: Int? = 10,
        @Query("busqueda") busqueda: String? = null
    ): Response<RespuestaProductos>

    @POST("productos")
    suspend fun crearProducto(
        @Header("Authorization") token: String,
        @Body producto: SolicitudCrearProducto
    ): Response<RespuestaCrearProducto>
}