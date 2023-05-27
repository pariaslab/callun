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
    val phaseInt = (phase * 15.0).roundToInt()
    val drawableId = when (phaseInt) {
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