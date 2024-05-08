package com.hw.orgpad.Data

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import com.hw.orgpad.Timetable.TimetableElement


@Database(entities = [TimetableElement::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tableDao(): TableDao
}