package com.hw.orgpad.Achievements

import com.hw.orgpad.Goals.Goal

class Achievement(goal: Goal) {

    var name: String
    var description: String

    init {
        name = goal.name
        description = goal.description
    }

    fun setNewValues(name: String, description: String) {
        this.name = name
        this.description = description
    }

    override fun toString(): String {
        return name
    }
}
