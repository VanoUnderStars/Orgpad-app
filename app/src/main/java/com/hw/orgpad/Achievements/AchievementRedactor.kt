package com.hw.orgpad.Achievements

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.hw.orgpad.CustomDialogFragment
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.R

class AchievementRedactor : AppCompatActivity() {

    internal var ID: Int = 0
    internal lateinit var nameField: EditText
    internal lateinit var descriptionField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_redactor)
        nameField = findViewById<View>(R.id.name) as EditText
        descriptionField = findViewById<View>(R.id.description) as EditText
        ID = intent.extras!!.get(EXTRA_ID) as Int

        val ach = OrgPadDatabaseHelper.achievements[ID]

        val nameText = ach.name
        val descriptionText = ach.description

        nameField.setText(nameText)
        descriptionField.setText(descriptionText)
    }

    fun onClickFin(view: View) {
        val name = nameField.text.toString()
        val description = descriptionField.text.toString()

        OrgPadDatabaseHelper.achievements[ID].setNewValues(name, description)
        OrgPadDatabaseHelper.exportToJSON(this, "achievements")
        super.finish()
    }

    fun onClickDelete(view: View) {
        val string = "Удалить достижение? Как поступать с ними - ваше право."
        val dialog = CustomDialogFragment(string, 4, ID, 0)
        dialog.show(supportFragmentManager, "custom")
    }

    companion object {

        val EXTRA_ID = "ID"
    }
}
