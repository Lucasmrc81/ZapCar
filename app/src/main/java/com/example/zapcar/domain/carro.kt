@file:Suppress("ktlint:standard:filename")

package com.example.zapcar.domain

data class Carro(
    val id: Int,
    val marca: String,
    val modelo: String,
    val preco: String,
    val precoNumerico: Double,
    val bateria: String,
    val potencia: String,
    val recarga: String,
    val urlPhoto: String,
    var isFavorite: Boolean,
) {
    companion object {
        val id: Carro
            get() {
                TODO()
            }
    }
}
