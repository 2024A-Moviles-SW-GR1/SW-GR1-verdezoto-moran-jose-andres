package org.example

import java.io.File

class EquipoCRUD(private val equipoCsvFile: String, private val jugadorCRUD: JugadorCRUD) {

    fun createEquipo(equipo: Equipo) {
        val file = File(equipoCsvFile)
        if (!file.exists()) {
            file.createNewFile()
        }

        val newId = generateNewId()
        val newLine = "${newId},${equipo.nombre},${equipo.nomEntrenador},${equipo.puntos}\n"
        file.appendText(newLine)
    }

    fun readEquipos(): List<Equipo> {
        val file = File(equipoCsvFile)
        if (!file.exists()) {
            return emptyList()
        }
        val equipos = mutableListOf<Equipo>()
        file.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size >= 4) {
                val id = tokens[0].toInt()
                val nombre = tokens[1]
                val nomEntrenador = tokens[2]
                val puntos = tokens[3].toInt()
                val jugadores = jugadorCRUD.read().filter { it.equipoId == id }.toMutableList()
                val equipo = Equipo(id, nombre, nomEntrenador, puntos, jugadores)
                equipos.add(equipo)
            }
        }
        return equipos
    }

    fun updateEquipo(equipo: Equipo) {
        val equipos = readEquipos().toMutableList()
        val index = equipos.indexOfFirst { it.id == equipo.id }
        if (index != -1) {
            equipos[index] = equipo
            saveAllEquipos(equipos)
        }
    }

    fun updateEquipoPuntos(equipoId: Int, resultado: Resultado) {
        val equipos = readEquipos().toMutableList()
        val index = equipos.indexOfFirst { it.id == equipoId }
        if (index != -1) {
            val equipo = equipos[index]
            var nuevosPuntos=0;
            if(resultado.equals(Resultado.Victoria)){
                nuevosPuntos=3
            } else if (resultado.equals(Resultado.Empate)){
                nuevosPuntos=1
            }else{
                nuevosPuntos=0
            }
            val puntajeActual = equipo.puntos
            val equipoActualizado = equipo.copy(puntos = nuevosPuntos+puntajeActual)
            equipos[index] = equipoActualizado
            saveAllEquipos(equipos)
        }
    }

    fun deleteEquipo(id: Int) {
        val equipos = readEquipos().filter { it.id != id }
        saveAllEquipos(equipos)
    }

    private fun saveAllEquipos(equipos: List<Equipo>) {
        val file = File(equipoCsvFile)
        file.writeText(equipos.joinToString("\n") { equipo ->
            "${equipo.id},${equipo.nombre},${equipo.nomEntrenador},${equipo.puntos}"
        })
    }

    private fun generateNewId(): Int {
        val jugadores = readEquipos()
        return if (jugadores.isEmpty()) {
            1
        } else {
            jugadores.maxOf { it.id } + 1
        }
    }
}