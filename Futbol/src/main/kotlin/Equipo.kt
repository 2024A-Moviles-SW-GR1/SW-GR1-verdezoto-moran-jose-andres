package org.example

data class Equipo(
    val id: Int,
    val nombre: String,
    val nomEntrenador: String,
    val puntos: Int,
    val jugadores: MutableList<Jugador> = mutableListOf(),
) {
    override fun toString(): String {
        return "\n" +
                "\t" + nombre.capitalize() + "\n" +
                "Entrenador: " + nomEntrenador.capitalize() + "\n" +
                "Puntos: " + puntos + "\n" +
                "Jugadores:\n" + "{" + jugadores.joinToString() { it.toString() + "\n}" } + "\n"
    }
}