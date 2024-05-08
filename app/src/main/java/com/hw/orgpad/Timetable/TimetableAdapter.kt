package com.hw.orgpad.Timetable

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hw.orgpad.R
import com.hw.orgpad.Tasks.Task

class TimetableAdapter:ArrayAdapter<TimetableElement> {

    private val inflater:LayoutInflater
    private val layout:Int
    private val table:MutableList<TimetableElement>

    constructor(context:Context, resource:Int, table:MutableList<TimetableElement>) : super(context, resource, table)
    {
        this.table = table
        this.layout = resource
        this.inflater = LayoutInflater.from(context)
    }

    override fun getView(position:Int, convertView:View?, parent:ViewGroup?):View {
        val view = inflater.inflate(this.layout, parent, false)
        val timeView = view.findViewById(R.id.time) as TextView
        val nameView = view.findViewById(R.id.name) as TextView
        val impView = view.findViewById(R.id.imp) as TextView
        val descrView = view.findViewById(R.id.descr) as TextView
        val tableElement = table.get(position)
        timeView.setText(tableElement.start.toString() + " - " + tableElement.end.toString())
        nameView.setText(tableElement.task.getTitle())
        impView.setText(tableElement.task.getImportance())
        descrView.setText(tableElement.task.getDescr())
        return view
    }
}