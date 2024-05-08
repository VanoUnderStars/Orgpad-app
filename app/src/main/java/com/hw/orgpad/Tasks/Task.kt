package com.hw.orgpad.Tasks

import com.hw.orgpad.R
import com.hw.orgpad.Time.Time


class Task {
    var description: String
    lateinit var name: String
    var importance: Int = 0
    var duration: Int = 0
    var difficulty: Int = 0
    @Transient
    var imageID: Int = 0
        internal set
    @Transient
    internal var coefficient: Int = 0
    internal var curTime: Time
    internal var maxTime: Time
    var isActive = true
    internal var completed: Boolean = false
    var goal_id: Int = 0

    internal val isComlplete: Boolean
        get() = curTime.isNull


    constructor() {
        description = "Здесь должно было быть описание"
        importance = 0
        duration = 0
        maxTime = Time(1, 0, 0)
        curTime = maxTime
        completed = false
    }

    constructor(name: String, description: String, ID: Int, importance: Int, active: Boolean, duration: Int, difficulty: Int) {
        this.description = description
        this.name = name
        //this.imageID = imageID;
        this.importance = importance
        this.isActive = active
        this.duration = duration
        this.difficulty = difficulty
        goal_id = ID
        maxTime = Time(0, 0, 0)
        curTime = maxTime
        completed = false
    }

    fun setNewValues(name: String, description: String, importance: Int, active: Boolean, duration: Int, difficulty: Int) {
        this.description = description
        this.name = name
        this.importance = importance
        this.isActive = active
        this.duration = duration
        this.difficulty = difficulty
        /*maxTime = new Time(hours,0,0);
        curTime = maxTime;*/
    }

    fun setGoalID(id: Int) {
        goal_id = id
    }

    fun getTitle():String {
        return name
    }

    fun getImgToShow():Int {

        if(completed)
            return R.drawable.checkmark
        when (importance + (difficulty *2)) {
            in 3..4 -> return R.drawable.very_easy
            in 5..6 -> return R.drawable.easy
            in 7..8 -> return R.drawable.moderate
            in 9..10 -> return R.drawable.hard
            else -> return R.drawable.very_hard
        }
    }

    fun getDescr():String {
        if(description.length > 60)
            return description.substring(0,  60) + "..."
        else
            return description
    }

    fun getImportance():String {
        when (importance) {
            1 -> return "Важность: Низкая"
            2 -> return "Важность: Второстепенная"
            3 -> return "Важность: Средняя"
            4 -> return "Важность: Высокая"
            else -> return "Важность: Очень высокая"
        }
    }

    fun createCoef(priority: Int) {
        coefficient = importance * priority
    }

    fun setCompleted() {
        completed = true
        isActive = false
    }

    override fun toString(): String {
        return name
    }
}