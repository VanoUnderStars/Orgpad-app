package com.hw.orgpad.Time

class Time {
    var hours: Int = 0
        internal set
    var minutes: Int = 0
        internal set
    var secundes: Int = 0
        internal set

    val sec: Int
        get() {
            var s = secundes
            var h = hours
            var m = minutes

            while (h > 0) {
                s += 3600
                h--
            }

            while (m > 0) {
                s += 60
                m--
            }

            return s
        }

    val min: Int
        get() {
            var h = hours
            var m = minutes

            while (h > 0) {
                m += 60
                h--
            }

            return m
        }

    val isNull: Boolean
        get() = hours <= 0 && minutes <= 0 && secundes <= 0

    constructor() {
        hours = 0
        minutes = 0
        secundes = 0
    }

    constructor(hours: Int, minutes: Int, secundes: Int) {
        this.hours = hours
        this.minutes = minutes
        this.secundes = secundes
    }

    fun addTime(h: Int, m: Int, s: Int) {
        secundes += s
        while (secundes > 59) {
            secundes -= 60
            minutes++
        }

        minutes += m
        while (minutes > 59) {
            minutes -= 60
            hours++
        }

        hours += h
    }

    fun removeTime(h: Int, m: Int, s: Int) {
        hours -= h

        minutes -= m
        while (minutes < 0) {
            minutes += 60
            hours--
        }

        secundes -= s
        while (secundes < 59) {
            secundes += 60
            minutes--
        }
    }

    fun setTime(h: Int, m: Int, s: Int) {
        secundes = s
        minutes = m
        hours = h
    }

    fun setTime(time: Time) {
        secundes = time.secundes
        minutes = time.minutes
        hours = time.hours
    }

    fun reset() {
        hours = 0
        minutes = 0
        secundes = 0
    }

    fun roundUp() {
        secundes = 0

        while (minutes % 5 != 0)
            addTime(0, 1, 0)
    }

    fun less(time: Time): Boolean {
        return hours < time.hours || hours == time.hours && minutes < time.minutes || hours == time.hours && minutes == time.minutes && secundes < time.secundes
    }

    fun more(time: Time): Boolean {
        return hours > time.hours || hours == time.hours && minutes > time.minutes || hours == time.hours && minutes == time.minutes && secundes > time.secundes
    }

    override fun toString(): String {
        return String.format("%d:%02d", hours, minutes)
    }
}
