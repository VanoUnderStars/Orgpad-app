package com.hw.orgpad.Tasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.hw.orgpad.CustomDialogFragment
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.MainActivity
import com.hw.orgpad.R
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Timetable.Timetable


class TaskView : AppCompatActivity() {
    //private val cursor: Cursor? = null
    internal var TASK_ID: Int = 0
    internal var GOAL_ID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view)
        TASK_ID = intent.extras!!.get("PREV_ID") as Int
        GOAL_ID = intent.extras!!.get("EXTRA_ID") as Int
        setSupportActionBar(findViewById(R.id.task_view_toolbar))
        getSupportActionBar()?.setTitle(OrgPadDatabaseHelper.goals[GOAL_ID].name+"/"+OrgPadDatabaseHelper.goals[GOAL_ID].tasks[TASK_ID].name);
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
                val string = ("    Здесь можно просмотреть информацию о задаче." +
                        "\n    Нажмите на кнопу «Изменить», чтобы редактировать задачу.")
                val dialog = CustomDialogFragment(string, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onDestroy() {
        super.onDestroy()
        //        cursor.close();
    }

    override fun onStart() {
        super.onStart()

        val task = OrgPadDatabaseHelper.goals[GOAL_ID].tasks[TASK_ID]
        val arr = arrayOf("Очень низкая", "Низкая", "Средняя", "Высокая", "Очень высокая")
        val nameText = task.name
        val descriptionText = task.description
        val importanceText = "Важность задачи: " + arr[task.importance - 1]
        val difficultyText = "Сложность задачи: " + arr[task.difficulty]
        val durationText = "Продолжительность одного подхода: " + arr[task.duration]
        //int photoId = task.getImageID();


        val name = findViewById<View>(R.id.name) as TextView
        name.text = nameText

        val description = findViewById<View>(R.id.description) as TextView
        description.text = descriptionText

        val priority = findViewById<View>(R.id.priority) as TextView
        priority.text = importanceText

        val difficulty = findViewById<View>(R.id.difficulty) as TextView
        difficulty.text = difficultyText

        val duration = findViewById<View>(R.id.duration) as TextView
        duration.text = durationText


        //ImageView photo = (ImageView)findViewById(R.id.photo);
        //photo.setImageResource(photoId);
    }

    fun onClickRedact(view: View) {
        val intent = Intent(this, TaskRedactor::class.java)
        intent.putExtra("EXTRA_ID", GOAL_ID)
        intent.putExtra("PREV_ID", TASK_ID)
        startActivity(intent)
    }

}
