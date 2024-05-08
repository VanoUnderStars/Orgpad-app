package com.hw.orgpad.Settings

import com.hw.orgpad.Time.Time

import java.util.Calendar
import java.util.Date

class Settings {

    var dif: Int = 0
    var primarily: Int = 0
    var week: BooleanArray
    var daynight: Boolean = false
    var start: Time
    var end: Time

    init {
        week = BooleanArray(8)
        start = Time(7, 0, 0)
        end = Time(15, 0, 0)
        dif = 1
        primarily = 1
        daynight = false
    }

    fun setTime(week: BooleanArray, start: Time, end: Time) {
        this.week = week
        this.start = start
        this.end = end
    }

    fun setDiffry(dif: Int) {
        this.dif = dif
    }

    fun checkTime(startTime: Time, endTime: Time): Boolean {
        val date = Date()
        val check: Boolean
        val c = Calendar.getInstance()
        c.time = date
        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        check = !(week[dayOfWeek] && (startTime.less(end) && startTime.more(start) || endTime.less(end) && endTime.more(start)))
        return check
    }
}
