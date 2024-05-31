package org.example

import java.util.Date

data class Jugador(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: Date,
    val posicion: Posicion,
    val dorsal: Int,
    val nacionalidad: String,
    val titular: Boolean,
    val equipoId: Int
){

    override fun toString(): String {
        val equipoNombre = obtenerNombreEquipo(equipoId)
        return "\n" +
                "\nJUGADOR N." + id + "\n\n\t"+
                nombre.capitalize() + " " + apellido.capitalize() + "\n" +
                "Fecha de Nacimiento: " + FuncionesAyuda.formatoFecha(fechaNacimiento) + "\n" +
                "Nacionalidad: " + nacionalidad.capitalize() + "\n" +
                "Posicion: " + posicion + "\n" +
                "Dorsal: " + dorsal + "\n" +
                "Equipo: " + equipoNombre + "\n" +
                "Titular: " + if(titular) "Si" else "No" + "\n"
    }

    private fun obtenerNombreEquipo(equipoId: Int): String {
        val equipoCRUD = EquipoCRUD("Equipos.csv", JugadorCRUD("Jugadores.csv"))
        val equipo = equipoCRUD.readEquipos().find { it.id.toInt() == equipoId }
        return equipo?.nombre ?: "Desconocido"
    }
}
