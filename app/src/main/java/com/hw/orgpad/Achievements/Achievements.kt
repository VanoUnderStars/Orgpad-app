package com.hw.orgpad.Achievements

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.hw.orgpad.*
import com.hw.orgpad.Data.OrgPadDatabaseHelper
import com.hw.orgpad.Goals.GoalRedactor
import com.hw.orgpad.Goals.GoalsList
import com.hw.orgpad.Settings.SettingsActivity
import com.hw.orgpad.Timetable.Timetable

class Achievements : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)
        setSupportActionBar(findViewById(R.id.achieve_toolbar))
        getSupportActionBar()?.setTitle("Зал достижений");
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val list = findViewById<View>(R.id.achievment_list) as ListView

        val listAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                OrgPadDatabaseHelper.achievements)

        list.adapter = listAdapter

        list.onItemLongClickListener = AdapterView.OnItemLongClickListener { arg0, arg1, pos, id ->
            // TODO Auto-generated method stub
            val intent = Intent(this@Achievements, AchievementRedactor::class.java)
            intent.putExtra(GoalRedactor.EXTRA_ID, pos)
            startActivity(intent)

            true
        }
    }
}
