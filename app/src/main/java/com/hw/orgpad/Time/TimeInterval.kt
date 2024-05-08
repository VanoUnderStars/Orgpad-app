package com.hw.orgpad.Time

import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Tasks.Task
import java.util.Random

class TimeInterval(internal var start: Time, internal var end: Time) {
    internal var prevstart: Time

    init {
        this.prevstart = Time()
    }


    fun insertTaskFront(task: Task, minutes: Int) {
        var minutes = minutes
        prevstart.setTime(start.hours, start.minutes, 0)
        val random = Random()
        val rand: Int
        val density = OrgPadDatabaseHelper.settings.dif

        when (density) {
            2 -> {
                rand = 5 * random.nextInt(4)
                prevstart.addTime(0, rand + 10, 0)
                minutes += rand + 10
            }
            3 -> {
                rand = 5 * random.nextInt(2)
                prevstart.addTime(0, rand + 5, 0)
                minutes += rand + 5
            }
            else -> {
                rand = 5 * random.nextInt(6)
                prevstart.addTime(0, rand + 20, 0)
                minutes += rand + 20
            }
        }

        this.start.addTime(0, minutes, 0)

    }

    fun insertTaskBack(task: Task, minutes: Int) {
        var minutes = minutes
        prevstart.setTime(start.hours, start.minutes, 0)
        val random = Random()
        val rand: Int
        val density = OrgPadDatabaseHelper.settings.dif

        when (density) {
            2 -> {
                rand = 5 * random.nextInt(5)
                prevstart.addTime(0, rand + 10, 0)
                minutes += rand + 10
            }
            3 -> {
                rand = 5 * random.nextInt(3)
                prevstart.addTime(0, rand + 5, 0)
                minutes += rand + 5
            }
            else -> {
                rand = 5 * random.nextInt(9)
                prevstart.addTime(0, rand + 20, 0)
                minutes += rand + 20
            }
        }

        this.start.addTime(0, minutes, 0)

    }
}
