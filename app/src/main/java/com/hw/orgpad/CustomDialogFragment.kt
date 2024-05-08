package com.hw.orgpad

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.hw.orgpad.Achievements.Achievement
import com.hw.orgpad.Data.OrgPadDatabaseHelper

@SuppressLint("ValidFragment")
class CustomDialogFragment @SuppressLint("ValidFragment")
constructor(internal var message: String, internal var type: Int, internal var goal: Int, internal var task: Int) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        return builder
                .setTitle("Справка")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setNegativeButton("Отмена", null)
                .setPositiveButton("OK") { dialog, which ->
                    when (type) {
                        2 -> {
                            OrgPadDatabaseHelper.achievements.add(Achievement(OrgPadDatabaseHelper.goals[goal]))
                            OrgPadDatabaseHelper.goals.removeAt(goal)
                            OrgPadDatabaseHelper.exportToJSON(this.context,"goals")
                            OrgPadDatabaseHelper.exportToJSON(this.context,"achievements")
                            OrgPadDatabaseHelper.exportToJSON(this.context,"tasks")
                            this.activity.finish()
                        }
                        3 -> {
                            OrgPadDatabaseHelper.goals[goal].tasks.removeAt(task)
                            OrgPadDatabaseHelper.exportToJSON(this.context,"tasks")
                            this.activity.finish()
                        }
                        4 -> {
                            OrgPadDatabaseHelper.achievements.removeAt(goal)
                            OrgPadDatabaseHelper.exportToJSON(this.context,"achievements")
                            this.activity.finish()
                        }
                        5 -> {
                            OrgPadDatabaseHelper.goals.removeAt(goal)
                            OrgPadDatabaseHelper.exportToJSON(this.context,"goals")
                            this.activity.finish()
                        }
                        6 -> {
                            OrgPadDatabaseHelper.goals[goal].tasks[task].setCompleted()
                            OrgPadDatabaseHelper.exportToJSON(this.context,"tasks")
                            this.activity.finish()
                        }
                    }
                }
                .create()
    }
}