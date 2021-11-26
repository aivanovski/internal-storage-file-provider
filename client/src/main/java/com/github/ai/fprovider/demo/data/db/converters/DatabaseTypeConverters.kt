package com.github.ai.fprovider.demo.data.db.converters

import androidx.room.TypeConverter
import java.util.Date

object DatabaseTypeConverters {

    @TypeConverter
    fun convertDateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun convertLongToDate(value: Long): Date {
        return Date(value)
    }
}