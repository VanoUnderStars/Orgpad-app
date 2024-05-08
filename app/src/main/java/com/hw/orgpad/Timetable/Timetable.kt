package com.hw.orgpad.Timetable

import android.arch.persistence.room.Room
import java.util.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.hw.orgpad.CustomDialogFragment
import com.hw.orgpad.Data.AppDatabase
import com.hw.orgpad.Goals.Goal
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.MainActivity
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Tasks.*
import com.hw.orgpad.Time.*
import java.util.ArrayList
import java.util.Random
import com.hw.orgpad.R







class Timetable : AppCompatActivity() {

    private val applicants = ArrayList<Task>()
    private var table = ArrayList<String>()
    private val random = Random()
    internal var curTasksNum: Int = 0

    private val isWorkDay: Boolean
        get() {
            val newCal = GregorianCalendar()
            var day = newCal.get(Calendar.DAY_OF_WEEK) - 1
            day--
            if (day == -1)
                day = 6
            return OrgPadDatabaseHelper.settings.week[day]
        }

    private val finishTime: Time
        get() {
            val hours: Int
            if (OrgPadDatabaseHelper.settings.daynight)
                hours = 24
            else
                hours = 22
            return Time(hours, 0, 0)
        }

    private val startTime: Time
        get() {
            val hours: Int
            if (OrgPadDatabaseHelper.settings.daynight)
                hours = 10
            else
                hours = 6
            val time = Time(hours, 0, 0)
            val curTime = currentTime

            return if (curTime.less(time))
                time
            else
                curTime
        }

    private/*gcalendar.get(Calendar.HOUR_OF_DAY)*/ val currentTime: Time
        get() {
            val gcalendar = GregorianCalendar()
            return Time(11, gcalendar.get(Calendar.MINUTE), gcalendar.get(Calendar.SECOND))
        }

    private val tasksNum: Int
        get() = if (isWorkDay)
            OrgPadDatabaseHelper.settings.dif * 6
        else
            OrgPadDatabaseHelper.settings.dif * 10

    fun generateTable() {
        if (getSum(currentTime) == 0)
        //Если нет задач
            table.add("Похоже что на сегодня задач нет.")
        else {
            curTasksNum = 0
            var interval: TimeInterval
            val beginTime: Time
            var endTime = Time()

            if (isWorkDay) { //Рабочий или выходной день?
                beginTime = startTime //Получение текущего времени либо близлежайшего свободного времени
                beginTime.roundUp() //Округление минут до количества, делящегося на пять
                endTime.setTime(OrgPadDatabaseHelper.settings.start)//Получение начала рабочих часов
                interval = TimeInterval(beginTime, endTime)
                putTasksInInterval(interval) //Заполнение задачами интервала до начала рабочих часов (Если он существует)

                beginTime.setTime(OrgPadDatabaseHelper.settings.end) //Получение конца рабочих часов
                beginTime.roundUp()
                endTime = finishTime //Получение времени конца дня
                interval = TimeInterval(beginTime, endTime)
                putTasksInInterval(interval) //Заполнение задачами интервала от конца рабочих часов до конца дня (Если он существует)
            } else {
                beginTime = startTime
                beginTime.roundUp()
                endTime = finishTime
                interval = TimeInterval(beginTime, endTime)
                putTasksInInterval(interval) //Заполнение задачами интервала от начала до конца дня
            }
        }

    }

    fun createView() {
        val timeList = findViewById<View>(R.id.list) as ListView

        if (getSum(currentTime) == 0) {
        val listAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                table)
        timeList.adapter = listAdapter//Вывод данных на экран
        return
        }

        val tableAdapter = TimetableAdapter(this, R.layout.goal_list_item, OrgPadDatabaseHelper.timetable)

        timeList.setAdapter(tableAdapter)

        val itemClickListener = AdapterView.OnItemClickListener { listView, itemView, position, id ->
            val intent = Intent(this@Timetable, TaskView::class.java)
            intent.putExtra("EXTRA_ID", table[position])
            startActivity(intent)
        }
        timeList.onItemClickListener = itemClickListener

        timeList.onItemLongClickListener = AdapterView.OnItemLongClickListener { arg0, arg1, pos, id ->
            val intent = Intent(this@Timetable, TimetableRedactor::class.java)
            intent.putExtra("EXTRA_ID", pos)
            startActivity(intent)

            true
        }
    }

    fun rewriteTable() {
        val strings = ArrayList<String>()
        for (t in OrgPadDatabaseHelper.timetable) {
            strings.add(t.toString())
        }
        table = strings
    }

    override fun onResume() {
        super.onResume()
        if (getSum(currentTime)!= 0)
        {
            rewriteTable()
            createView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        if (savedInstanceState != null) {
            table = savedInstanceState.getStringArrayList("key_table")
        }
        else if(OrgPadDatabaseHelper.timetable.size != 0)
            rewriteTable()
        else
            generateTable()


            createView()

        setSupportActionBar(findViewById(R.id.table_toolbar))
        getSupportActionBar()?.setTitle("Расписание задач");
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
            com.hw.orgpad.R.id.item_help -> {
                val string = ("    Кнопка «Сгенерировать» создает расписание заново." +
                        "\n    Долгое нажатие по пункту расписания позволяет отредактировать его.")
                val dialog = CustomDialogFragment(string, 1, 0, 0)
                dialog.show(supportFragmentManager, "custom")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("Timetable", "onSaveInstanceState called")
        outState.putStringArrayList("key_table", table)
    }

    private fun putTasksInInterval(interval: TimeInterval) {
        println(interval.start.toString() + " - " + interval.end)
        var sum: Int
        var choise: Task?
        var start: Time
        var end: Time
        var element: TimetableElement
//        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
 //       val tableDao = db.tableDao()
        OrgPadDatabaseHelper.timetable.clear()
        while (curTasksNum < tasksNum && interval.start.less(interval.end)) {
            sum = getSum(interval.start) //Получение суммы коэффициентов всех задач для данного момента времени
            choise = getChoise(sum, interval.start) //Получение случайной задачи (Чем выше коэффициент, тем выше вероятность получения)

            val time = getRandMinutes(choise!!.duration) //Получение случайной продолжительности задачи в зависимости от параметра "Продолжительность"
            //System.out.println(time);
            interval.insertTaskFront(choise, time) //Получение случайной продолжительности задачи в зависимости от параметра "Продолжительность"
            if (interval.start.less(interval.end)) {
                table.add(interval.prevstart.toString() + " - " + interval.start + " : " + choise)
                start = Time(interval.prevstart.hours, interval.prevstart.minutes, 0)
                end = Time(interval.start.hours, interval.start.minutes, 0)
                element = TimetableElement(start, end, choise)
                OrgPadDatabaseHelper.timetable.add(element)
                print(OrgPadDatabaseHelper.timetable)
               // tableDao!!.insert(element)
               // print(tableDao.toString())
            }
            else
                curTasksNum -= getTaskValue(choise)
            curTasksNum += getTaskValue(choise)
        }
    }

    private fun getRandMinutes(duration: Int): Int {
        val rand: Int
        when (duration) {
            2 -> rand = 30 + 5 * random.nextInt(6)
            3 -> rand = 60 + 5 * random.nextInt(6)
            else -> rand = 15 + 5 * random.nextInt(3)
        }
        return rand
    }

    private fun getSum(time: Time): Int {
        var sum = 0
        for (i in OrgPadDatabaseHelper.goals.indices)
            if (OrgPadDatabaseHelper.goals[i].isActive)
                for (j in OrgPadDatabaseHelper.goals[i].tasks.indices) {
                    if (OrgPadDatabaseHelper.goals[i].tasks[j].isActive)
                        sum += getPriorityCoeff(OrgPadDatabaseHelper.goals[i], OrgPadDatabaseHelper.goals[i].tasks[j], time)
                }

        return sum
    }

    private fun getPriorityCoeff(goal: Goal, task: Task, time: Time): Int {
        var result = goal.priority * task.importance
        if (tasksNum / 2 > curTasksNum && OrgPadDatabaseHelper.settings.primarily == 3 && task.difficulty == 3
                || tasksNum / 2 > curTasksNum && OrgPadDatabaseHelper.settings.primarily == 1 && task.difficulty == 1
                || tasksNum / 2 < curTasksNum && OrgPadDatabaseHelper.settings.primarily == 1 && task.difficulty == 3
                || tasksNum / 2 < curTasksNum && OrgPadDatabaseHelper.settings.primarily == 3 && task.difficulty == 1)
            result *= 5
        if (OrgPadDatabaseHelper.settings.dif == 3 && task.difficulty == 3 || OrgPadDatabaseHelper.settings.dif == 1 && task.difficulty == 1)
            result *= 2
        return result
    }

    private fun getTaskValue(task: Task): Int {
        return task.difficulty + task.duration
    }

    private fun getChoise(sum: Int, time: Time): Task? {
        val rand = random.nextInt(sum)
        var s = 0
        var choise: Task? = null


        var i = 0
        while (i < OrgPadDatabaseHelper.goals.size && s <= rand) {
            if (OrgPadDatabaseHelper.goals[i].isActive) {
                var j = 0
                while (j < OrgPadDatabaseHelper.goals[i].tasks.size && s <= rand) {
                    if (OrgPadDatabaseHelper.goals[i].tasks[j].isActive) {
                        s += getPriorityCoeff(OrgPadDatabaseHelper.goals[i], OrgPadDatabaseHelper.goals[i].tasks[j], time)
                        choise = OrgPadDatabaseHelper.goals[i].tasks[j]
                    }
                    j++
                }
            }
            i++
        }

        return choise
    }



    fun onClickRefactor(view: View) {
        table.clear()
        generateTable()
        createView()
    }
}
