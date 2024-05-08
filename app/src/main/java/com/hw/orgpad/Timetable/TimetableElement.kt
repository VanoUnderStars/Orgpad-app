package com.hw.orgpad.Timetable

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.hw.orgpad.Tasks.Task
import com.hw.orgpad.Time.Time

@Entity
class TimetableElement {

    @PrimaryKey
    var id: Long = 0

    @ColumnInfo
    var start: Time = Time()
        internal set
    @ColumnInfo
    var end: Time = Time()
        internal set
    @ColumnInfo
    var task: Task = Task()
        internal set


    constructor() {
        start = Time()
        end = Time()
        task = Task()
    }

    constructor(start: Time, end: Time, task: Task) {
        this.start = start
        this.end = end
        this.task = task
    }

    override fun toString(): String {
        return String.format(start.toString() + " - " + end.toString() + " : " + task.getTitle())
    }
}
