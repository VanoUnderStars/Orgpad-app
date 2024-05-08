package com.hw.orgpad.Timetable

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.app.TimePickerDialog
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.hw.orgpad.CustomDialogFragment
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.MainActivity
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.R
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Time.Time
import android.widget.ArrayAdapter
import com.hw.orgpad.Tasks.Task
import java.util.ArrayList


class TimetableRedactor : AppCompatActivity() {

    internal lateinit var startView: TextView
    internal lateinit var endView: TextView
    internal lateinit var taskSpin: Spinner
    internal lateinit var curElement: TimetableElement
    var tasks: MutableList<Task> = ArrayList()
    internal var ID: Int = 0

    fun onClickSave(view: View) {

        curElement.task = tasks[taskSpin.selectedItemPosition]

        OrgPadDatabaseHelper.timetable[ID] = curElement

        this.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable_redactor)
        ID = intent.extras.get("EXTRA_ID") as Int

        taskSpin = findViewById<View>(R.id.task_title) as Spinner
        tasks = OrgPadDatabaseHelper.getAllTasks()
        var tasks_s: ArrayList<String> = ArrayList()

        for(task in tasks)
            tasks_s.add(task.toString())

        curElement = OrgPadDatabaseHelper.timetable[ID]

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tasks_s)
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Применяем адаптер к элементу spinner
        taskSpin.setAdapter(adapter)

        taskSpin.setSelection(tasks.indexOf(curElement.task))



        startView = findViewById<View>(R.id.starthour) as TextView
        endView = findViewById<View>(R.id.endhour) as TextView
        startView.setText(curElement.start.toString())
        endView.setText(curElement.end.toString())

        setSupportActionBar(findViewById(R.id.table_redactor_toolbar))
        getSupportActionBar()?.setTitle("Редактирование расписания");
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
                val reference = ("    Не нравится задача или время? Тогда выберите другие!")
                val dialog = CustomDialogFragment(reference, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setTime(v: View) {
        if (v == startView)
            TimePickerDialog(this, s,
                    curElement.start.hours,
                    curElement.start.minutes, true)
                    .show()
        else
            TimePickerDialog(this, e,
                    curElement.end.hours,
                    curElement.end.minutes, true)
                    .show()
    }

    // установка обработчика выбора времени
    @RequiresApi(Build.VERSION_CODES.N)
    var s: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        var time = Time(hourOfDay, minute, 0)
        if (!time.more(curElement.end)) {
            curElement.start = time
            startView.setText(curElement.start.toString())
        } else {
            val toast = Toast.makeText(applicationContext,
                    "Ошибка! Начальное время больше конечного.",
                    Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    var e: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        var time = Time(hourOfDay, minute, 0)
        if (!time.less(curElement.start)) {
            curElement.end = time
            endView.setText(curElement.end.toString())
        } else {
            val toast = Toast.makeText(applicationContext,
                    "Ошибка! Конечное время меньше начального.",
                    Toast.LENGTH_SHORT)
            toast.show()
        }
    }

}
