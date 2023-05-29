package com.pariaslab.callun

import com.example.callun.R
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.roundToInt

fun getMoonLapse(date: LocalDate): Double {
    // New moon base
    val baseDate = LocalDate.of(2000, 1, 6)
    val daysSinceBase = ChronoUnit.DAYS.between(baseDate, date)
    val synodicCicle = 29.53058867 // Average days of a synodic cicle
    val phase = (daysSinceBase % synodicCicle) / synodicCicle
    return phase
}

fun getMoonLapse(date: Calendar): Double {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return getMoonLapse(localDate)
}

fun getMoonLapseIcon(date: Calendar): Int {
    val phase = getMoonLapse(date)
    val phaseInt = (phase * 55.0).roundToInt()
    val resourceName = "frame_$phaseInt"
    val packageName = "com.example.callun"
    return try {
        val field = Class.forName("$packageName.R\$drawable").getField(resourceName)
        field.getInt(null)
    } catch (e: Exception) {
        e.printStackTrace()
        R.drawable.ic_launcher_foreground
    }
}
