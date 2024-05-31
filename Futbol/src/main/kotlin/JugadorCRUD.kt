package org.example

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class JugadorCRUD(
    val csvFile: String
) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun create(jugador: Jugador) {
        val file = File(csvFile)
        if (!file.exists()) {
            file.createNewFile()
        }
        val newId = generateNewId()
        val newLine = "${newId},${jugador.nombre},${jugador.apellido},${dateFormat.format(jugador.fechaNacimiento)},${jugador.posicion},${jugador.dorsal},${jugador.nacionalidad},${jugador.titular},${jugador.equipoId}\n"
        file.appendText(newLine)
    }

    fun read(): List<Jugador> {
        val file = File(csvFile)
        if(!file.exists()){
            return emptyList()
        }
        val jugadoresLeidos = mutableListOf<Jugador>()
        file.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size == 9) {
                    val id = tokens[0].toInt()
                    val nombre = tokens[1]
                    val apellido = tokens[2]
                    val fechaNacimiento = dateFormat.parse(tokens[3])
                    val posicion = Posicion.valueOf(tokens[4])
                    val dorsal = tokens[5].toInt()
                    val nacionalidad = tokens[6]
                    val titular = tokens[7].toBoolean()
                    val equipoId = tokens[8].toInt()
                    val jugador = Jugador(id, nombre, apellido, fechaNacimiento, posicion, dorsal, nacionalidad, titular, equipoId)
                    jugadoresLeidos.add(jugador)
            }
        }
        return jugadoresLeidos
    }

    fun update(jugador: Jugador, nuevoEquipoId: Int? = null) {
        val jugadores = read().toMutableList()
        val index = jugadores.indexOfFirst { it.id == jugador.id }
        if (index != -1) {
            val jugadorExistente = jugadores[index]
            val jugadorActualizado = jugadorExistente.copy(
                nombre = jugador.nombre,
                apellido = jugador.apellido,
                fechaNacimiento = jugador.fechaNacimiento,
                posicion = jugador.posicion,
                dorsal = jugador.dorsal,
                nacionalidad = jugador.nacionalidad,
                titular = jugador.titular,
                equipoId = nuevoEquipoId ?: jugador.equipoId
            )

            jugadores[index] = jugadorActualizado
            saveAll(jugadores)

//            if (nuevoEquipoId != null && nuevoEquipoId != jugador.equipoId) {
//                val equipoCRUD = EquipoCRUD("Equipos.csv", this)
//                val equipos = equipoCRUD.readEquipos().toMutableList()
//
//                val equipoViejo = equipos.find { it.id.toInt() == jugador.equipoId }
//                equipoViejo?.jugadores?.removeIf { it.id == jugador.id }
//                equipoViejo?.let { equipoCRUD.updateEquipo(it) }
//
//                val equipoNuevo = equipos.find { it.id.toInt() == nuevoEquipoId }
//                equipoNuevo?.jugadores?.add(jugadorActualizado)
//                equipoNuevo?.let { equipoCRUD.updateEquipo(it) }
//            }
        }
    }

    fun delete(id: Int) {
        val jugadores = read().filter { it.id != id }
        saveAll(jugadores)
    }

    private fun saveAll(jugadores: List<Jugador>) {
        val file = File(csvFile)
        file.writeText(jugadores.joinToString("\n") { jugador ->
            "${jugador.id},${jugador.nombre},${jugador.apellido},${dateFormat.format(jugador.fechaNacimiento)},${jugador.posicion},${jugador.dorsal},${jugador.nacionalidad},${jugador.titular},${jugador.equipoId}"
        })
    }

    private fun generateNewId(): Int {
        val jugadores = read()
        return if (jugadores.isEmpty()) {
            1
        } else {
            jugadores.maxOf { it.id } + 1
        }
    }
}