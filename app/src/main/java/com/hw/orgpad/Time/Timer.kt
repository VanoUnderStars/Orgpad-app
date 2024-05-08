package com.hw.orgpad.Time

import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.hw.orgpad.R

class Timer : AppCompatActivity() {
    private val time = Time()
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean("running")
            time.addTime(0, 0, savedInstanceState.getInt("time"))
        }
        runTimer()
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("time", time.sec)
        savedInstanceState.putBoolean("running", running)
    }

    override fun onStop() {
        super.onStop()
        running = false
    }

    private fun runTimer() {
        val timeView = findViewById<View>(R.id.time_view) as TextView

        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                timeView.text = time.toString()
                if (running) {
                    time.addTime(0, 0, 1)
                }
                handler.postDelayed(this, 10)
            }
        })
    }

    fun onClickStart(view: View) {
        running = true
    }

    fun onClickPause(view: View) {
        running = false
    }

    fun onClickStop(view: View) {
        running = false
        time.reset()
    }
}
