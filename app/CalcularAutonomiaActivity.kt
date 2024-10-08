package com.example.eletriccarapp.ui


import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.zapcar.R

class CalcularAutonomiaActivity : AppCompatActivity() {

    lateinit var preco: EditText
    lateinit var kmPercorrido: EditText
    lateinit var resultado: TextView
    lateinit var btnCalcular: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupView()
        setupListeners()

    }


    fun setupView() {
        preco = findViewById(R.id.Et_kmh)
        kmPercorrido = findViewById(R.id.Et_km_percorrido)
        resultado = findViewById(R.id.Tv_resultado)
        btnCalcular = findViewById(R.id.btn_calcular)

    }

    fun setupListeners() {
        btnCalcular.setOnClickListener {
            calcular()
        }

    }

    fun calcular() {
        val preco = preco.text.toString().toFloat()
        val km = kmPercorrido.text.toString().toFloat()
        val result = preco / km

        resultado.text = result.toString()

    }


}



