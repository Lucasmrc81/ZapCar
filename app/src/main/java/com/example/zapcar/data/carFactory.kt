@file:Suppress("ktlint:standard:filename")

package com.example.zapcar.data

import com.example.zapcar.domain.Carro

object CarFactory {
    val list =
        listOf(
            Carro(
                id = 1,
                marca = "Marca1",
                modelo = "Modelo1",
                precoNumerico = 299999.99,
                preco = "R$ 299.999,99",
                bateria = "40,75 Kwh",
                potencia = "184 Cv",
                recarga = "30 min",
                urlPhoto = "https://www.google.com.br",
                isFavorite = false,
            ),
            Carro(
                id = 2,
                marca = "Marca2",
                modelo = "Modelo2",
                precoNumerico = 319999.99,
                preco = "R$ 319.999,99",
                bateria = "67,45 Kwh",
                potencia = "169 Cv",
                recarga = "30 min",
                urlPhoto = "https://www.google.com.br",
                isFavorite = false,
            ),
        )
}
