package com.example.semana8_web.apiservices

import com.example.semana8_web.clases.Vehiculo

import  retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    //reservada para invocar a los verbos http (CRUD)
    //CREAR
    @POST(value = "vehiculos/create.php")
    suspend fun crear(@Body vehiculo: Vehiculo): Map<String, Any>
    //Read
    @GET(value = "vehiculos/get.php")
    suspend fun getVehiculos(): List<Vehiculo>
    //Actualizar
    @PUT(value="vehiculos/update.php")
    suspend fun actualizar(@Body vehiculo: Vehiculo): Map<String, String>
    //Eliminar
    @DELETE(value= "vehiculos/delete.php")
    suspend fun eliminar(@Query("id")id: Int): Map<String, String>
}