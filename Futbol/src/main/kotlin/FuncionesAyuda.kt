package org.example

import java.text.SimpleDateFormat
import java.util.Date

class FuncionesAyuda {
    companion object {
        fun formatoFecha(date: Date): String {
            val formato = "yyyy/MM/dd"
            val simpleDateFormat = SimpleDateFormat(formato)
            val dateFormatted = simpleDateFormat.format(date)
            return dateFormatted
        }
    }
}