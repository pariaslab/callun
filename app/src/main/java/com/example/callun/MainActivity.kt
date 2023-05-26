package com.example.callun

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.ZoneId
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun actualizarIconoFaseLunar(fecha: Calendar, resource: Int) {
            val lunarPhaseIcon = findViewById<ImageView>(resource)
            val faseLunar = obtenerFaseLunar(fecha)
            val faseInt: Int = obtenerFaseInt(faseLunar)
            val drawableId = obtenerFaseIcon(faseInt)
            lunarPhaseIcon.setImageResource(drawableId)
        }


        // Obtener la fecha actual
        val currentCalendar = Calendar.getInstance()
        // Retroceder una semana
        currentCalendar.add(Calendar.DAY_OF_MONTH, -7)
        var selectedDate = currentCalendar

        val selectDateButton = findViewById<Button>(R.id.selectDateButton)
        selectDateButton.text = obtenerTextoFechaSeleccionada(selectedDate)
        actualizarIconoFaseLunar(selectedDate, R.id.histLunarImg)
        selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = selectedCalendar
                    actualizarIconoFaseLunar(selectedDate, R.id.histLunarImg)
                    selectDateButton.text = obtenerTextoFechaSeleccionada(selectedDate)
                    val faseLunarHist = obtenerFaseLunar(selectedDate)
                    val histFaseLunar = findViewById<TextView>(R.id.histFaseLunar)
                    val textoHistFaseLunar: String = String.format("%.0f %% del ciclo desde luna nueva", faseLunarHist*100.0)
                    histFaseLunar.text = textoHistFaseLunar
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


        // Obtener la fecha actual
        val fechaActual = Calendar.getInstance()
        val faseLunarActual = obtenerFaseLunar(fechaActual)

        // Mostrar la fase lunar actual en el texto
        val tvFaseLunar = findViewById<TextView>(R.id.tvFaseLunar)
        val textoFaseLunar: String = String.format("%.0f %% del ciclo desde luna nueva", faseLunarActual*100.0)
        tvFaseLunar.text = textoFaseLunar

        // Mostrar icono de fase actual
        actualizarIconoFaseLunar(fechaActual, R.id.actualLunarImg)

    }
}


fun obtenerFaseLunar(fecha: LocalDate): Double {
    val fechaBase = LocalDate.of(2000, 1, 6) // Fecha base para el cálculo
    val diasDesdeBase = ChronoUnit.DAYS.between(fechaBase, fecha)
    val cicloSinodico = 29.53058867 // Duración promedio de un ciclo sinódico lunar en días
    val fase = (diasDesdeBase % cicloSinodico) / cicloSinodico
    return fase
}

fun obtenerFaseInt(fase: Double): Int {
    return (fase * 15.0).roundToInt()
}

fun obtenerFaseLunar(fecha: Calendar): Double {
    val fechaLocalDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return obtenerFaseLunar(fechaLocalDate)
}

fun obtenerFaseIcon(faseInt: Int): Int {
    val drawableId = when (faseInt) {
        0 -> R.drawable.moon0
        1 -> R.drawable.moon1
        2 -> R.drawable.moon2
        3 -> R.drawable.moon3
        4 -> R.drawable.moon4
        5 -> R.drawable.moon5
        6 -> R.drawable.moon6
        7 -> R.drawable.moon7
        8 -> R.drawable.moon8
        9 -> R.drawable.moon9
        10 -> R.drawable.moon10
        11 -> R.drawable.moon11
        12 -> R.drawable.moon12
        13 -> R.drawable.moon13
        14 -> R.drawable.moon14
        15 -> R.drawable.moon15
        else -> R.drawable.ic_launcher_foreground
    }
    return drawableId
}

fun obtenerIconoFaseLunar(fecha: Calendar) : Int {
    val faseLunar = obtenerFaseLunar(fecha)
    val faseInt: Int = obtenerFaseInt(faseLunar)
    val drawableId = obtenerFaseIcon(faseInt)
    return drawableId
}

private fun obtenerTextoFechaSeleccionada(fecha: Calendar): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(fecha.time)
}