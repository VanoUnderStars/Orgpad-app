package com.hw.orgpad.Settings

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
import com.hw.orgpad.*
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.Time.Time
import com.hw.orgpad.Timetable.Timetable


class SettingsActivity : AppCompatActivity() {

    internal lateinit var startView: TextView
    internal lateinit var endView: TextView
    internal lateinit var start: Time
    internal lateinit var end: Time
    internal var checkboxid = intArrayOf(R.id.checkbox1, R.id.checkbox2, R.id.checkbox3, R.id.checkbox4, R.id.checkbox5, R.id.checkbox6, R.id.checkbox7)
    internal lateinit var diffSpin: Spinner
    internal lateinit var primarilySpin: Spinner
    internal lateinit var dayOrNight: Switch

    fun onClickSave(view: View) {
        OrgPadDatabaseHelper.settings.dif = 3 - diffSpin.selectedItemPosition
        OrgPadDatabaseHelper.settings.primarily = 3 - primarilySpin.selectedItemPosition
        OrgPadDatabaseHelper.settings.daynight = dayOrNight.isChecked

        val week = BooleanArray(8)

        var checkbox: CheckBox
        for (i in 0..6) {
            checkbox = findViewById<View>(checkboxid[i]) as CheckBox
            week[i] = checkbox.isChecked
        }

        OrgPadDatabaseHelper.settings.setTime(week, start, end)

        OrgPadDatabaseHelper.exportToJSON(this, "settings")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

         diffSpin = findViewById<View>(R.id.difficulty) as Spinner
        primarilySpin = findViewById<View>(R.id.primarily) as Spinner
        diffSpin.setSelection(3 - OrgPadDatabaseHelper.settings.dif)
        primarilySpin.setSelection(3 - OrgPadDatabaseHelper.settings.primarily)

        dayOrNight = findViewById<View>(R.id.dayOrNight) as Switch
        dayOrNight.isChecked = OrgPadDatabaseHelper.settings.daynight

        start = OrgPadDatabaseHelper.settings.start
        end = OrgPadDatabaseHelper.settings.end
        startView = findViewById<View>(R.id.starthour) as TextView
        endView = findViewById<View>(R.id.endhour) as TextView
        startView.setText(OrgPadDatabaseHelper.settings.start.toString())
        endView.setText(OrgPadDatabaseHelper.settings.end.toString())

        var checkbox: CheckBox
        for (i in 0..6) {
            checkbox = findViewById<View>(checkboxid[i]) as CheckBox
            checkbox.isChecked = OrgPadDatabaseHelper.settings.week[i]
        }
        setSupportActionBar(findViewById(R.id.settings_toolbar))
        getSupportActionBar()?.setTitle("Настройки расписания");
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
                val reference = ("    Загруженность определяет количество и сложность задач, которые появляются в расписании."
                        + "\n   Рабочие часы неприкасаемы для расписания"
                        + "\n   Режим жаворонка создан для тех, кто готов выполнять задачи с раннего утра. Если же вы любите что-то делать допозна, а с утра хотите только спать, то режим совы - ваш выбор.")
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
                    start.hours,
                    start.minutes, true)
                    .show()
        else
            TimePickerDialog(this, e,
                    end.hours,
                    end.minutes, true)
                    .show()
    }

    // установка обработчика выбора времени
    @RequiresApi(Build.VERSION_CODES.N)
    var s: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        var time = Time(hourOfDay, minute, 0)
        if (!time.more(end)) {
            start = time
            startView.setText(start.toString())
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
        if (!time.less(start)) {
            end = time
            endView.setText(end.toString())
        } else {
            val toast = Toast.makeText(applicationContext,
                    "Ошибка! Конечное время меньше начального.",
                    Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun showDialog(v: View) {

        val reference = ("    Загруженность определяет количество и сложность задач, которые появляются в расписании."
                + "\n   Рабочие часы неприкасаемы для расписания"
                + "\n   Режим жаворонка создан для тех, кто готов выполнять задачи с раннего утра. Если же вы любите что-то делать допозна, а с утра хотите только спать, то режим совы - ваш выбор.")
        val dialog = CustomDialogFragment(reference, 1, 0, 0)
        dialog.show(supportFragmentManager, "custom")
    }
}
