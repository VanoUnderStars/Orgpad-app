package com.hw.orgpad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.hw.orgpad.Achievements.Achievements
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Timetable.Timetable
import java.io.File







class MainActivity :  AppCompatActivity() {
    private val serverRequest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        getSupportActionBar()?.setTitle("Orgpad");

        if (OrgPadDatabaseHelper.goals.size == 0)
        {
            if (!File(this.getFilesDir().getPath().toString() + "/goals.json").exists())
                createFiles()
            readGoals()
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
                return true
            }
            R.id.item_help -> {
                val string = ("    Нажмите на кнопку «Список целей», чтобы приступить к созданию целей и задач." +
                        "\n    Нажмите на кнопку «Расписание», чтобы составить расписание из имеющихся задач." +
                        "\n    Нажмите на кнопку «Настройки расписания», чтобы настроить автоматическое распределение задач.")
                val dialog = CustomDialogFragment(string, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickSettings(view: View) {
        //if (OrgPadDatabaseHelper.goals.size != 0) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        /*} else {
            val toast = Toast.makeText(applicationContext,
                    "В списке нет целей!",
                    Toast.LENGTH_SHORT)
            toast.show()
        }*/
    }

    fun createFiles() {
        File(this.getFilesDir().getPath().toString() + "/goals.json").createNewFile()
        File(this.getFilesDir().getPath().toString() + "/settings.json").createNewFile()
        File(this.getFilesDir().getPath().toString() + "/tasks.json").createNewFile()
        File(this.getFilesDir().getPath().toString() + "/achievements.json").createNewFile()
    }

    fun readGoals() {
        try {
            OrgPadDatabaseHelper.sendGet(this)
            OrgPadDatabaseHelper.refreshIDs()
        } catch (e: Exception) {
            e.printStackTrace()
            val toast = Toast.makeText(applicationContext,
                    "Ошибка подключения!",
                    Toast.LENGTH_SHORT)
            toast.show()
        }

    }


    fun onClickTimetable(view: View) {
        val intent = Intent(this, Timetable::class.java)
        startActivity(intent)
    }

    fun onClickGoals(view: View) {
        val intent = Intent(this, GoalsList::class.java)
        startActivity(intent)
    }

    fun onClickAchievements(view: View) {
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }

}

