package com.hw.orgpad.Goals

import com.hw.orgpad.R
import com.hw.orgpad.Tasks.Task
import java.util.ArrayList

class Goal {
    var name: String
    var description: String
    @Transient
    var tasks: MutableList<Task> = ArrayList()
    var priority: Int = 0
    var id: Int = 0
    var isActive: Boolean = false
        private set

    constructor() {
        name = "-"
        description = "-"
        isActive = true
    }

    constructor(name: String, description: String, priority: Int, active: Boolean) {
        this.name = name
        this.description = description
        this.priority = priority
        this.isActive = active
    }

    fun setNewValues(name: String, description: String, priority: Int, active: Boolean) {
        this.name = name
        this.description = description
        this.priority = priority
        this.isActive = active
    }

    fun setID(id: Int) {
        this.id = id
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun replaceTask(task: Task, id: Int) {
        tasks[id] = task
    }

    fun getTask(i: Int): Task {
        return tasks[i]
    }

    override fun toString(): String {
        return name
    }

    fun getTitle():String {
        return name
    }

    fun getImgToShow():Int {

        if(isActive == false)
            return R.drawable.unactive
        when (priority) {
            1 -> return R.drawable.very_low
            2 -> return R.drawable.low
            3 -> return R.drawable.medium
            4 -> return R.drawable.high
            else -> return R.drawable.extra
        }
    }

    fun getDescr():String {
        if(description.length > 60)
            return description.substring(0,  60) + "..."
        else
            return description
    }
}
