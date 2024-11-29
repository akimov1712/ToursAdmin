package ru.topbun.toursadmin.utills

import io.ktor.util.date.GMTDate
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun GMTDate.formatDate() = "${String.format("%02d",this.dayOfMonth)}.${String.format("%02d",this.month.ordinal + 1)}.${this.year}"

fun String.parseToGTMDate(): GMTDate{
    val parts = this.split(".")
    val day = parts[0].toInt()
    val month = parts[1].toInt() - 1
    val year = parts[2].toInt()
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
    calendar.set(year, month, day, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return GMTDate(calendar.timeInMillis)
}