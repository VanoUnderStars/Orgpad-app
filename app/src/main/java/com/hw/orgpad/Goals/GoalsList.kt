package com.hw.orgpad.Goals

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.hw.orgpad.*
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Tasks.TasksList
import com.hw.orgpad.Timetable.Timetable


class GoalsList : AppCompatActivity() {

    //private SQLiteDatabase db;
    //private Cursor cursor;
    internal var value = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_goals)
        setSupportActionBar(findViewById(R.id.goals_toolbar))
        getSupportActionBar()?.setTitle("Список целей");
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
                val string = ("    Нажмите на кнопку «Добавить», чтобы создать новую цель." +
                        "\n    Нажатие на цель открывает список задач для этой цели." +
                        "\n    Долгое нажатие открывает экран редактирования цели." +
                        "\n    Погоны показывают приоритет цели. Выше звание - выше приоритет.")
                val dialog = CustomDialogFragment(string, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        val listGoals = findViewById<View>(R.id.goal_list) as ListView
        /*val listAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                OrgPadDatabaseHelper.goals)
        listGoals.adapter = listAdapter*/
        // создаем адаптер
        val taskAdapter = GoalAdapter(this, R.layout.task_list_item, OrgPadDatabaseHelper.goals)
        // устанавливаем адаптер
        listGoals.setAdapter(taskAdapter)

        val itemClickListener = AdapterView.OnItemClickListener { listView, itemView, position, id ->
            val intent = Intent(this@GoalsList, TasksList::class.java)
            intent.putExtra("EXTRA_ID", position)
            startActivity(intent)
        }
        listGoals.onItemClickListener = itemClickListener

        listGoals.onItemLongClickListener = AdapterView.OnItemLongClickListener { arg0, arg1, pos, id ->
            // TODO Auto-generated method stub
            val intent = Intent(this@GoalsList, GoalRedactor::class.java)
            intent.putExtra("EXTRA_ID", pos)
            startActivity(intent)

            true
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    fun onClickAdd(view: View) {
        val intent = Intent(this, GoalRedactor::class.java)
        intent.putExtra("EXTRA_ID", 1000)
        startActivity(intent)
    }


}
