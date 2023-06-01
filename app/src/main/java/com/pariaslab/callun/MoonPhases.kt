package com.pariaslab.callun

import android.util.Log
import com.example.callun.R
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPosition
import org.shredzone.commons.suncalc.MoonTimes
import org.shredzone.commons.suncalc.SunTimes
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.roundToInt


fun getMoonLapse2(date: LocalDate): Double {
    // New moon base
    val baseDate = LocalDate.of(2000, 1, 6)
    val daysSinceBase = ChronoUnit.DAYS.between(baseDate, date)
    val synodicCicle = 29.53058867 // Average days of a synodic cicle
    val phase = (daysSinceBase % synodicCicle) / synodicCicle
    return phase
}

fun getMoonLapse2(date: Calendar): Double {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return getMoonLapse2(localDate)
}

fun getMoonLapseIcon(date: Calendar): Int {
    val phase = getMoonLapse(date).phase
    val phaseInt = (((phase+180) * 55.0)/360).roundToInt()
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


fun getMoonLapse(date: Calendar): MoonIllumination {
    val times: MoonIllumination = MoonIllumination.compute()
        .on(date) // set a date
        .execute() // get the results
    return times
}

fun getMoonPosition(date: Calendar): MoonPosition {
    val position: MoonPosition = MoonPosition.compute()
        .on(date) // set a date
        .localTime()
        .execute() // get the results
    return position
}

fun getMoonTimes(date: Calendar): MoonTimes {
    val times: MoonTimes = MoonTimes.compute()
        .on(date)
        .localTime()
        .execute()
    return times
}