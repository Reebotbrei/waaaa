package com.example.semana8_web.clases
import java.io.Serializable

data class Vehiculo(
    val Id: Int,
    val Marca: String,
    val Modelo: String,
    val Precio: Double,
    val Fabricacion: Int,
    val Foto: String?       //? <> Acepta nulo
): Serializable
