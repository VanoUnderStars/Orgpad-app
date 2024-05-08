package com.hw.orgpad.Tasks

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Switch
import com.hw.orgpad.*
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Timetable.Timetable

class TaskRedactor : AppCompatActivity() {

    internal var ID: Int = 0
    internal var oldID: Int = 0
    internal lateinit var nameField: EditText
    internal lateinit var descriptionField: EditText
    internal lateinit var importanceSpin: Spinner
    internal lateinit var durationSpin: Spinner
    internal lateinit var difficultySpin: Spinner
    internal lateinit var activeSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_redactor)
        setSupportActionBar(findViewById(R.id.task_redactor_toolbar))
        nameField = findViewById<View>(R.id.name) as EditText
        descriptionField = findViewById<View>(R.id.description) as EditText
        importanceSpin = findViewById<View>(R.id.importance) as Spinner
        durationSpin = findViewById<View>(R.id.duration) as Spinner
        difficultySpin = findViewById<View>(R.id.task_difficulty) as Spinner
        activeSwitch = findViewById<View>(R.id.active) as Switch
        val delete = findViewById<View>(R.id.delete) as ImageButton
        val complete = findViewById<View>(R.id.complete) as ImageButton

        ID = intent.extras.get("EXTRA_ID") as Int
        oldID = intent.extras.get("PREV_ID") as Int

        if (oldID != 1000) {
            val task = OrgPadDatabaseHelper.goals[ID].tasks[oldID]

            val nameText = task.name
            val descriptionText = task.description
            val importance = task.importance
            val duration = task.duration
            val difficulty = task.difficulty
            val active = task.isActive

            nameField.setText(nameText)
            descriptionField.setText(descriptionText)
            importanceSpin.setSelection(5 - importance)
            durationSpin.setSelection(3 - duration)
            difficultySpin.setSelection(3 - difficulty)
            if(task.completed)
            {
                complete.setClickable(false)
                complete.visibility = View.GONE
                activeSwitch.setClickable(false)
            }
            else
                activeSwitch.isChecked = active
            getSupportActionBar()?.setTitle(nameText + "/Редактор");
        } else {
            activeSwitch.isChecked = true
            importanceSpin.setSelection(2)
            durationSpin.setSelection(1)
            difficultySpin.setSelection(1)
            delete.visibility = View.GONE
            complete.visibility = View.GONE
            getSupportActionBar()?.setTitle("Создание задачи");
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
                val string = ("   Важность задачи определяет то, насколько необходима данная задача для достижения цели. Важные задачи чаще появляются в расписании"
                        + "\n   Длина подходов определяет количество времени, которое нужно потратить на задачу. Задачи могут продолжатся от 15 до 120 минут."
                        + "\n   Сложность задачи определяет её трудоемкость и трудозатратность. Сложные задачи появляются в незагруженное время."
                        + "\n   Только продолжительные задачи появляются в расписании.")
                val dialog = CustomDialogFragment(string, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun showReference(v: View) {
        val string = ("   Важность задачи определяет то, насколько необходима данная задача для достижения цели. Важные задачи чаще появляются в расписании"
                + "\n   Длина подходов определяет количество времени, которое нужно потратить на задачу. Задачи могут продолжатся от 15 до 120 минут."
                + "\n   Сложность задачи определяет её трудоемкость и трудозатратность. Сложные задачи появляются в незагруженное время."
                + "\n   Только продолжительные задачи появляются в расписании.")
        val dialog = CustomDialogFragment(string, 1, 0, 0)
        dialog.show(supportFragmentManager, "custom")
    }

    fun onClickFin(view: View) {

        val name = nameField.text.toString()
        val description = descriptionField.text.toString()
        val importance = 5 - importanceSpin.selectedItemPosition
        val duration = 3 - durationSpin.selectedItemPosition
        val difficulty = 3 - difficultySpin.selectedItemPosition
        val active = activeSwitch.isChecked

        if (oldID == 1000) {
            OrgPadDatabaseHelper.goals[ID].addTask(Task(name, description, ID + 1, importance, active, duration, difficulty))
        } else {
            OrgPadDatabaseHelper.goals[ID].tasks[oldID].setNewValues(name, description, importance, active, duration, difficulty)
        }

        OrgPadDatabaseHelper.exportToJSON(this, "tasks")
        super.finish()
    }

    fun onClickDelete(view: View) {
        val string = "Действительно удалить задачу?"
        val dialog = CustomDialogFragment(string, 3, ID, oldID)
        dialog.show(supportFragmentManager, "custom")
    }

    fun setCompleted(view: View) {
        val string = "Отметить задачу как выполненную? Она больше не сможет появлятся в расписании."
        val dialog = CustomDialogFragment(string, 6, ID, oldID)
        dialog.show(supportFragmentManager, "custom")
    }

}
