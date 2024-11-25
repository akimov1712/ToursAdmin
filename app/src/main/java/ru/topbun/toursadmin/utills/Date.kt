package ru.topbun.toursadmin.utills

import io.ktor.util.date.GMTDate

fun GMTDate.formatDate() = "${String.format("%02d",this.dayOfMonth)}.${String.format("%02d",this.month.ordinal)}.${this.year}"