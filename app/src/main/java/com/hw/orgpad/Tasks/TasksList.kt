package com.hw.orgpad.Tasks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.hw.orgpad.*
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Timetable.Timetable


class TasksList : AppCompatActivity() {
    // private SQLiteDatabase db;
    //  private Cursor cursor;
    internal var ID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ID = intent.extras.get("EXTRA_ID") as Int
        setContentView(R.layout.activity_tasks)
        setSupportActionBar(findViewById(R.id.tasks_toolbar))
        getSupportActionBar()?.setTitle(OrgPadDatabaseHelper.goals[ID].name + "/Задачи");
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
                val string = ("    Нажмите на кнопку «Добавить», чтобы создать новую задачу." +
                        "\n    Нажмите на задачу чтобы просмотреть её описание." +
                        "\n    Долгое нажатие открывает экран редактирования задачи." +
                        "\n    Рожица показывает сложность и важность задачи.")
                val dialog = CustomDialogFragment(string, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onStart() {
        super.onStart()
        val listTasks = findViewById<View>(R.id.task_list) as ListView
        //print(OrgPadDatabaseHelper.goals[0].tasks.toString() + "\n")
        /*val listAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                OrgPadDatabaseHelper.goals[ID].tasks)

        listTasks.adapter = listAdapter*/

        // создаем адаптер
        val taskAdapter = TaskAdapter(this, R.layout.task_list_item, OrgPadDatabaseHelper.goals[ID].tasks)
        // устанавливаем адаптер
        listTasks.setAdapter(taskAdapter)

        val itemClickListener = AdapterView.OnItemClickListener { listView, itemView, position, id ->
            val intent = Intent(this@TasksList, TaskView::class.java)
            intent.putExtra("PREV_ID", position)
            intent.putExtra("EXTRA_ID", ID)
            startActivity(intent)
        }
        listTasks.onItemClickListener = itemClickListener

        listTasks.onItemLongClickListener = AdapterView.OnItemLongClickListener { arg0, arg1, pos, id ->
            // TODO Auto-generated method stub
            val intent = Intent(this@TasksList, TaskRedactor::class.java)
            intent.putExtra("PREV_ID", pos)
            intent.putExtra("EXTRA_ID", ID)
            startActivity(intent)

            true
        }
    }


    public override fun onDestroy() {
        super.onDestroy()
    }

    fun onClickAdd(view: View) {
        val intent = Intent(this, TaskRedactor::class.java)
        intent.putExtra("EXTRA_ID", ID)
        intent.putExtra("PREV_ID", 1000)
        startActivity(intent)
    }

    companion object {
        val EXTRA_ID = "ID"
        val PREV_ID = "oldID"
    }


}
