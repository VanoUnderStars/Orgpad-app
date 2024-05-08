package com.hw.orgpad.Data

import android.arch.persistence.room.*
import com.hw.orgpad.Timetable.TimetableElement

@Dao
interface TableDao {

    @get:Query("SELECT * FROM table")
    val all: List<TimetableElement>

    @Query("SELECT * FROM table WHERE id = :id")
    fun getById(id: Long): TimetableElement

    @Query("DELETE FROM table WHERE id < 30")
    fun deleteAll(): TimetableElement

    @Insert
    fun insert(tableElem: TimetableElement)

    @Update
    fun update(tableElem: TimetableElement)

    @Delete
    fun delete(tableElem: TimetableElement)

}

