package org.example

import java.text.SimpleDateFormat
import java.util.*

fun main() {

    val jugadorCRUD = JugadorCRUD("Jugadores.csv")
    val equipoCRUD = EquipoCRUD("Equipos.csv", jugadorCRUD)
    val scanner = Scanner(System.`in`)

//    MENU

    var opcion: Int = 0;
    while(opcion != 9){
        mostrarMenu();
//        Leer opcion
        opcion = readLine()!!.toInt();
        when(opcion){
            1 -> {
//                Mostrar Jugadores
                println(jugadorCRUD.read())
            }
            2 -> {
//                Mostrar Equipos con jugadores
                println(equipoCRUD.readEquipos())
            }
            3 -> {
//                Mostrar lista de equipos
                val listaEquipos = equipoCRUD.readEquipos()
                println("Equipos Disponibles:")
                listaEquipos.forEach { println("${ it.id }:${it.nombre}") }
            }
            4 -> {
//                Ingresar nuevo jugador
                println("Ingrese el nombre del jugador:")
                val nombre = scanner.nextLine().capitalize()
                println("Ingrese el apellido del jugador:")
                val apellido = scanner.nextLine().capitalize()
                println("Ingrese la fecha de nacimiento del jugador (yyyy-MM-dd):")
                val fechaNacimientoStr = scanner.nextLine()
                val fechaNacimiento = SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimientoStr)
                println("Ingrese la posición del jugador (arquero, defensa, medio, delantero):")
                val posicionStr = scanner.nextLine()
                val posicion = Posicion.valueOf(posicionStr.capitalize())
                println("Ingrese el dorsal del jugador:")
                val dorsal = scanner.nextInt()
                scanner.nextLine()
                println("Ingrese la nacionalidad del jugador:")
                val nacionalidad = scanner.nextLine().capitalize()
                println("¿El jugador es titular? (si/no):")
                val titularStr = scanner.nextLine()
                val titular = if(titularStr == "si") true else false

                val listaEquipos = equipoCRUD.readEquipos()
                println("Equipos Disponibles:")
                listaEquipos.forEach { println("${ it.id }:${it.nombre}") }
                println("Escoge el equipo: ")
                val equipo = scanner.nextLine().toInt()

                val nuevoJugador = Jugador(
                    id = 0,
                    nombre = nombre,
                    apellido = apellido,
                    fechaNacimiento = fechaNacimiento,
                    posicion = posicion,
                    dorsal = dorsal,
                    nacionalidad = nacionalidad,
                    titular = titular,
                    equipoId = equipo
                )
                jugadorCRUD.create(nuevoJugador)
                println("Jugador creado!")

                val equipos = listaEquipos.find { it.id == equipo }
                if (equipos != null) {
                    equipos.jugadores.add(nuevoJugador)
                    equipoCRUD.updateEquipo(equipos)
                }
            }
            5 -> {
//                Modificar jugador
                println(jugadorCRUD.read())
                println("Ingrese el N. del jugador que desea actualizar:")
                val jugadorId = scanner.nextInt()
                scanner.nextLine() // Consumir la nueva línea

                val jugador = jugadorCRUD.read().find { it.id == jugadorId }
                if (jugador != null) {
                    val listaEquipos = equipoCRUD.readEquipos()
                    listaEquipos.forEach { println("${ it.id }:${it.nombre}") }
                    println("Escoge el nuevo del jugador(deje en blanco para no cambiar): ")
                    val equipoIdStr = scanner.nextLine()

                    val nuevoEquipoId = if (equipoIdStr.isNotBlank()) equipoIdStr.toInt() else jugador.equipoId

                    val jugadorActualizado = jugador.copy(
                        nombre = jugador.nombre,
                        apellido = jugador.apellido,
                        fechaNacimiento = jugador.fechaNacimiento,
                        posicion = jugador.posicion,
                        dorsal = jugador.dorsal,
                        nacionalidad = jugador.nacionalidad,
                        titular = jugador.titular,
                    )

                    jugadorCRUD.update(jugadorActualizado, nuevoEquipoId)
                    println("Jugador actualizado exitosamente.")
                } else {
                    println("No se encontró un jugador con ID: $jugadorId")
                }
            }
            6 -> {
//                Eliminar jugador
                println(jugadorCRUD.read())
                println("Ingrese el N. del jugador que desea eliminar:")
                val jugadorId = scanner.nextInt()
                jugadorCRUD.delete(jugadorId)
                println("Jugador eliminado correctamente!")
            }
            7 -> {
//                Ingresar nuevo equipo
                println("Ingrese el nombre del equipo:")
                val nombre = scanner.nextLine().capitalize()
                println("Ingrese el nombre del entrenador:")
                val nomEntrenador = scanner.nextLine().capitalize()
                println("Ingrese los puntos del equipo")
                val puntos = scanner.nextLine().toInt()

                val nuevoEquipo = Equipo(
                    id = 0,
                    nombre = nombre,
                    nomEntrenador = nomEntrenador,
                    puntos = puntos,
                )
                equipoCRUD.createEquipo(nuevoEquipo)
                println("Equipo creado correctamente!")
            }
            8 -> {
//                Modificar equipo
                val listaEquipos = equipoCRUD.readEquipos()
                println("Equipos Disponibles:")
                listaEquipos.forEach { println("${ it.id }:${it.nombre}") }
                print("\nIngrese el N. del equipo a registrar cambios:")
                val equipoId = scanner.nextLine().toInt()

                val equipo = equipoCRUD.readEquipos().find { it.id == equipoId.toInt() }
                if (equipo != null) {
                    println("Ingrese el resultado(victoria, empate, derrota:")
                    val resultado = scanner.nextLine()
                    val nuevosPuntos = Resultado.valueOf(resultado.capitalize())

                    scanner.nextLine() // Consumir la nueva línea

                    equipoCRUD.updateEquipoPuntos(equipoId, nuevosPuntos)
                    println("Puntos del equipo actualizados exitosamente.")
                } else {
                    println("No se encontró un equipo con ID: $equipoId")
                }
            }
            9 -> {
//                Eliminar equipo
                println(equipoCRUD.readEquipos())
                println("Ingrese el N. del equipo que desea eliminar:")
                val equipoId = scanner.nextInt()
                equipoCRUD.deleteEquipo(equipoId)
                println("Equipo eliminado correctamente")
            }
            10 -> {
//                Salir
                println("Adios!")
                break;
            }
            else -> continue
        }
    }


}

fun mostrarMenu(){
    println("\n\t"+ "******* FUTBOL *******\n" +
    "1. Mostrar Jugadores\n"+
    "2. Mostrar Equipos con Plantilla\n"+
    "3. Mostrar Lista de Equipos\n"+
    "4. Ingresar nuevo Jugador\n"+
    "5. Modificar jugador\n"+
    "6. Eliminar jugador\n"+
    "7. Ingresar nuevo Equipo\n"+
    "8. Registrar Resultado\n"+
    "9. Eliminar equipo\n"+
    "10. Salir\n"+
    "Ingresa tu opcion: ")
}

fun mostrarPosiciones(){
    println("Escoja la opcion\n" +
    "1. Arquero\n"+
    "2. Defensa\n"+
    "3. Medio\n"+
    "4. Delantero\n"+
    "Opcion: ")
}