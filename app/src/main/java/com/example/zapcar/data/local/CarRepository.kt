package com.example.eletriccarapp.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.util.Log
import com.example.zapcar.data.local.CarrosContract
import com.example.zapcar.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.example.zapcar.data.local.CarrosContract.CarEntry.COLUMN_NAME_CAR_ID
import com.example.zapcar.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.example.zapcar.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.example.zapcar.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.example.zapcar.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.example.zapcar.data.local.CarsDbHelper
import com.example.zapcar.domain.Carro

class CarRepository(
    private val context: Context,
) {
    fun save(carro: Carro): Boolean {
        var isSaved = false
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.writableDatabase

        try {
            val values =
                ContentValues().apply {
                    put(COLUMN_NAME_CAR_ID, carro.id)
                    put(COLUMN_NAME_PRECO, carro.preco)
                    put(COLUMN_NAME_BATERIA, carro.bateria)
                    put(COLUMN_NAME_POTENCIA, carro.potencia)
                    put(COLUMN_NAME_RECARGA, carro.recarga)
                    put(COLUMN_NAME_URL_PHOTO, carro.urlPhoto)
                }

            val inserted = db.insert(CarrosContract.CarEntry.TABLE_NAME, null, values)
            if (inserted != -1L) {
                isSaved = true
            }
        } catch (ex: Exception) {
            Log.e("Erro ao inserir -> ", ex.message ?: "Erro desconhecido", ex)
        } finally {
            db.close()
        }

        return isSaved
    }

    fun findCarById(id: Int): Carro? {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase
        var carro: Carro? = null

        val columns =
            arrayOf(
                BaseColumns._ID,
                COLUMN_NAME_CAR_ID,
                COLUMN_NAME_PRECO,
                COLUMN_NAME_BATERIA,
                COLUMN_NAME_POTENCIA,
                COLUMN_NAME_RECARGA,
                COLUMN_NAME_URL_PHOTO,
            )

        val filter = "$COLUMN_NAME_CAR_ID = ?"
        val filterValues = arrayOf(id.toString())

        val cursor =
            db.query(
                CarrosContract.CarEntry.TABLE_NAME,
                columns,
                filter,
                filterValues,
                null,
                null,
                null,
            )

        try {
            if (cursor.moveToFirst()) {
                carro = cursorToCar(cursor)
            }
        } catch (ex: Exception) {
            Log.e("Erro ao buscar -> ", ex.message ?: "Erro desconhecido", ex)
        } finally {
            cursor.close()
            db.close()
        }

        return carro
    }

    fun saveIfNotExist(carro: Carro) {
        val car = findCarById(carro.id)
        if (car == null) {
            save(carro)
        }
    }

    fun getAll(): List<Carro> {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase
        val carros = mutableListOf<Carro>()

        val columns =
            arrayOf(
                BaseColumns._ID,
                COLUMN_NAME_CAR_ID,
                COLUMN_NAME_PRECO,
                COLUMN_NAME_BATERIA,
                COLUMN_NAME_POTENCIA,
                COLUMN_NAME_RECARGA,
                COLUMN_NAME_URL_PHOTO,
            )

        val cursor =
            db.query(
                CarrosContract.CarEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null,
            )

        try {
            while (cursor.moveToNext()) {
                val carro = cursorToCar(cursor)
                carros.add(carro)
            }
        } catch (ex: Exception) {
            Log.e("Erro ao listar -> ", ex.message ?: "Erro desconhecido", ex)
        } finally {
            cursor.close()
            db.close()
        }

        return carros
    }

    private fun cursorToCar(cursor: Cursor): Carro {
        val itemId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_CAR_ID))
        val preco = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_PRECO))
        val bateria = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_BATERIA))
        val potencia = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_POTENCIA))
        val recarga = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_RECARGA))
        val urlPhoto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_URL_PHOTO))

        return Carro(
            id = itemId.toInt(),
            preco = preco,
            bateria = bateria,
            potencia = potencia,
            recarga = recarga,
            urlPhoto = urlPhoto,
            isFavorite = true,
        )
    }

    private fun Carro(
        id: Int,
        preco: String?,
        bateria: String?,
        potencia: String?,
        recarga: String?,
        urlPhoto: String?,
        isFavorite: Boolean,
    ): Carro {
        TODO("Not yet implemented")
    }

    companion object {
        const val ID_WHEN_NO_CAR = 0
    }
}
