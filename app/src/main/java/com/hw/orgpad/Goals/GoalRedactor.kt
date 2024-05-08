package com.hw.orgpad.Goals

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Switch
import android.view.Menu
import android.view.MenuItem
import com.hw.orgpad.*
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Timetable.Timetable


class GoalRedactor : AppCompatActivity() {

    internal var ID: Int = 0
    internal lateinit var nameField: EditText
    internal lateinit var descriptionField: EditText
    internal lateinit var spinner: Spinner
    internal lateinit var activeSwitch: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_redactor)
        setSupportActionBar(findViewById(R.id.goals_redactor_toolbar))
        nameField = findViewById<View>(R.id.name) as EditText
        descriptionField = findViewById<View>(R.id.description) as EditText
        spinner = findViewById<View>(R.id.priority) as Spinner
        ID = intent.extras.get("EXTRA_ID") as Int
        activeSwitch = findViewById<View>(R.id.active) as Switch
        val delete = findViewById<View>(R.id.delete) as ImageButton

        spinner.setSelection(2)

        if (ID != 1000) {

            val goal = OrgPadDatabaseHelper.goals[ID]


            val nameText = goal.name
            val descriptionText = goal.description
            val importance = goal.priority
            val active = goal.isActive

            nameField.setText(nameText)
            descriptionField.setText(descriptionText)
            spinner.setSelection(5 - importance)

            activeSwitch.isChecked = active
            getSupportActionBar()?.setTitle(nameText + "/Редактор");
        } else {
            activeSwitch.isChecked = true
            delete.visibility = View.GONE
            getSupportActionBar()?.setTitle("Создание цели");
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        when (id) {
            R.id.item_goals -> {
                val intent = Intent(this, GoalsList::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_timetable -> {
                val intent = Intent(this, Timetable::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_main -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_help -> {
                val string = ("   Приоритет цели напрямую влияет на вероятность появления задач этой цели в расписании."
                        + "\n   Задачи неактивных целей не появляются в расписании.")
                val dialog = CustomDialogFragment(string, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun onClickFin(view: View) {
        val name = nameField.text.toString()
        val description = descriptionField.text.toString()
        val priority = 5 - spinner.selectedItemPosition
        val active = activeSwitch.isChecked

        if (ID == 1000) {

            OrgPadDatabaseHelper.goals.add(Goal(name, description, priority, active))
        } else
            OrgPadDatabaseHelper.goals[ID].setNewValues(name, description, priority, active)
        OrgPadDatabaseHelper.exportToJSON(this, "goals")
        super.finish()
    }

    fun onClickComplete(view: View) {
        val string = "Отметить цель как выполненную? Цель добавится к достижениям и будет навсегда удалена."
        val dialog = CustomDialogFragment(string, 2, ID, 0)
        dialog.show(supportFragmentManager, "custom")
    }

    fun onClickDelete(view: View) {
        val string = "Удалить цель? Это действие необратимо."
        val dialog = CustomDialogFragment(string, 5, ID, 0)
        dialog.show(supportFragmentManager, "custom")
    }

    companion object {

        val EXTRA_ID = "ID"
    }

}
