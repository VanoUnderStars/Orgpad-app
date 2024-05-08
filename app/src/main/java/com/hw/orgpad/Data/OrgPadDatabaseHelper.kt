package com.hw.orgpad.Data

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hw.orgpad.Achievements.Achievement
import com.hw.orgpad.Goals.Goal
import com.hw.orgpad.Settings.Settings
import com.hw.orgpad.Tasks.Task
import com.hw.orgpad.Timetable.TimetableElement

import java.util.ArrayList
import java.io.*


class OrgPadDatabaseHelper {


    fun getGoal(id: Int): Goal {
        return goals[id]
    }

    fun getTask(goalId: Int, taskId: Int): Task {
        return goals[goalId].tasks[taskId]
    }



    companion object {
        private val IP = "192.168.0.100:4567" //IP адрес сайта
        var goals: MutableList<Goal> = ArrayList()
        var settings = Settings()
        var achievements: MutableList<Achievement> = ArrayList()
        var timetable: MutableList<TimetableElement> = ArrayList()

        fun getAllTasks(): MutableList<Task> {

            var i = 0
            var tasks: MutableList<Task> = ArrayList()

            while (i < goals.size) {
                if (goals[i].isActive) {
                    var j = 0
                    while (j < goals[i].tasks.size) {
                        if (goals[i].tasks[j].isActive) {
                            tasks.add(goals[i].tasks[j])
                        }
                        j++
                    }
                }
                i++
            }
            return tasks
        }

        fun importFromJSON(FILE_NAME: String, context: Context): String {

            var streamReader: InputStreamReader? = null
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream = context.openFileInput(FILE_NAME)
                streamReader = InputStreamReader(fileInputStream!!)

                val gson = Gson()
                return streamReader.readText()
            } catch (ex: IOException) {
                ex.printStackTrace()
            } finally {
                if (streamReader != null) {
                    try {
                        streamReader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            return ""
        }

        fun exportToJSON(context: Context, FILE_NAME: String): Boolean {

            val gson = Gson()

            var fileOutputStream: FileOutputStream? = null

            val urlParameters: String

            when (FILE_NAME) {
                "goals" -> urlParameters = /*FILE_NAME + "=" +*/ gson.toJson(goals)
                "tasks" -> {
                    val tasks = ArrayList<Task>()
                    for (i in goals.indices)
                        for (j in goals[i].tasks.indices)
                            tasks.add(goals[i].tasks[j])
                    urlParameters = /*FILE_NAME + "=" +*/ gson.toJson(tasks)
                }
                "achievements" -> urlParameters = /*FILE_NAME + "=" +*/ gson.toJson(achievements)
                else -> urlParameters = /*FILE_NAME + "=" +*/ gson.toJson(settings)
            }

            try {
                val writer = PrintWriter(File(context.getFilesDir().getPath().toString() + "/" + FILE_NAME +".json"))
                writer.print("")
                writer.close()
                fileOutputStream = context.openFileOutput(FILE_NAME + ".json", Context.MODE_PRIVATE)
                fileOutputStream!!.write(urlParameters.toByteArray())
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            return false
        }

        fun setSettingsToDefault(context: Context): Boolean {

            val gson = Gson()

            var fileOutputStream: FileOutputStream? = null

            val urlParameters: String

            urlParameters = "settings={\"daynight\":true,\"diff\":2,\"end\":{\"hours\":15,\"minutes\":0,\"secundes\":0},\"primarily\":3,\"start\":{\"hours\":13,\"minutes\":0,\"secundes\":0},\"week\":[true,false,true,false,false,false,false,false]}"


            try {
                //if (!File( "settings.json").exists())
                val writer = PrintWriter(File(context.getFilesDir().getPath().toString() + "/settings.json"))
                writer.print("")
                writer.close()
                fileOutputStream = context.openFileOutput( "settings.json", Context.MODE_APPEND)
                fileOutputStream!!.write(urlParameters.toByteArray())
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            return false
        }
        // HTTP GET requests
        @Throws(Exception::class)
        fun sendGet(context: Context) {

            val thread = Thread(Runnable {
                try {
                    val tasksStr = importFromJSON("tasks.json", context)
                    print(tasksStr + "\n")
                    val goalsStr = importFromJSON("goals.json", context)
                    print(goalsStr + "\n")
                    val settings = importFromJSON("settings.json", context)
                    val achievements = importFromJSON("achievements.json", context)
                    readAll(tasksStr, goalsStr, settings, achievements)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
            thread.start()
        }

        /*fun setToDefault(context: Context) {

            val thread = Thread(Runnable {
                try {
                    val tasksStr = importFromJSON(context.getFilesDir().getPath().toString() +"/tasks.json", context)
                    val goalsStr = importFromJSON(context.getFilesDir().getPath().toString() +"/goals.json", context)
                    val settings = importFromJSON(context.getFilesDir().getPath().toString() +"/settings.json", context)
                    val achievements = importFromJSON( context.getFilesDir().getPath().toString() +"/achievements.json", context)
                    readAll(tasksStr, goalsStr, settings, achievements)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
            thread.start()
        }*/


        private fun readAll(tasksJsn: String, goalsJsn: String, settingsJsn: String, achievementsJsn: String) {

            val builder = GsonBuilder()
            val gson = builder.create()

            val taskarr = gson.fromJson<Array<Task>>(tasksJsn, Array<Task>::class.java)
            val goalarr = gson.fromJson<Array<Goal>>(goalsJsn, Array<Goal>::class.java)
            val acharr = gson.fromJson<Array<Achievement>>(achievementsJsn, Array<Achievement>::class.java)

            settings = gson.fromJson(settingsJsn, Settings::class.java)

            for (i in goalarr.indices) {
                goals.add(goalarr[i])
            }

            print(goals[0].tasks.toString() + "\n")

            for (i in taskarr.indices) {
                goals[taskarr[i].goal_id - 1].addTask(taskarr[i])
            }

            print(goals[0].tasks.toString() + "\n")

            for (i in acharr.indices) {
                achievements.add(acharr[i])
            }
        }

        fun refreshIDs() {
            for (i in goals.indices) {
                for (j in goals[i].tasks.indices)
                    goals[i].tasks[j].setGoalID(i)
                goals[i].setID(i)
            }
        }
    }

}
