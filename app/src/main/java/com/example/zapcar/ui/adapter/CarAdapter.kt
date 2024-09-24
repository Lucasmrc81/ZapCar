package com.example.zapcar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zapcar.R
import com.example.zapcar.domain.Carro

class CarAdapter(
    private val carros: List<Carro>,
    private val isFavoriteScreen: Boolean = false,
) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {
    var carItemLister: (Carro) -> Unit = {}

    // Cria uma nova view
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item, parent, false)
        return ViewHolder(view)
    }

    // Pega o conteudo da view e troca pela informacao de item de uma lista
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.preco.text = carros[position].preco
        holder.bateria.text = carros[position].bateria
        holder.potencia.text = carros[position].potencia
        holder.recarga.text = carros[position].recarga

        if (isFavoriteScreen) {
            holder.favorito.setImageResource(R.drawable.ic_star_selected)
        }

        holder.favorito.setOnClickListener {
            val carro = carros[position]
            carItemLister(carro)
            setupFavorite(carro, holder)
        }
    }

    private fun setupFavorite(
        carro: Carro,
        holder: ViewHolder,
    ) {
        carro.isFavorite = !carro.isFavorite
        if (carro.isFavorite) {
            holder.favorito.setImageResource(R.drawable.ic_star_selected)
        } else {
            holder.favorito.setImageResource(R.drawable.star_border)
        }
    }

    // Pega a quantidade de carros da lista
    override fun getItemCount(): Int = carros.size

    class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val preco: TextView
        val bateria: TextView
        val potencia: TextView
        val recarga: TextView
        val favorito: ImageView

        init {
            view.apply {
                preco = findViewById(R.id.Tv_preco_Value_1)
                bateria = findViewById(R.id.Tv_bateria_value_1)
                potencia = findViewById(R.id.tv_potencia_value_1)
                recarga = findViewById(R.id.tv_recarga_value_1)
                favorito = findViewById(R.id.iv_favorite)
            }
        }
    }
}
