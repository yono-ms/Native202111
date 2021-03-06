package com.example.native202111

import android.os.Build
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.fromIsoToDate(): Date {
    val isoFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        "yyyy-MM-dd'T'HH:mm:ssX"
    } else {
        "yyyy-MM-dd'T'HH:mm:ssX"
    }
    kotlin.runCatching {
        SimpleDateFormat(isoFormat, Locale.US).parse(this)
    }.onSuccess {
        return it ?: Date()
    }.onFailure {
        return Date(0)
    }
    return Date(0)
}

fun Date.toBestString(): String {
    kotlin.runCatching {
        val locale = Locale.getDefault()
        val cal = Calendar.getInstance(locale).apply { time = this@toBestString }
        val pattern = DateFormat.getBestDateTimePattern(locale, "yyyyMMMdEEEHHmmss")
        SimpleDateFormat(pattern, locale).format(cal.time)
    }.onSuccess {
        return it
    }.onFailure {
        return it.message ?: ""
    }
    return ""
}
