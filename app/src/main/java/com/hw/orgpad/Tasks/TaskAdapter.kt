package com.hw.orgpad.Tasks

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hw.orgpad.R

class TaskAdapter:ArrayAdapter<Task> {

    private val inflater:LayoutInflater
    private val layout:Int
    private val tasks:MutableList<Task>

    constructor(context:Context, resource:Int, tasks:MutableList<Task>) : super(context, resource, tasks)
    {
        this.tasks = tasks
        this.layout = resource
        this.inflater = LayoutInflater.from(context)
    }

    override fun getView(position:Int, convertView:View?, parent:ViewGroup?):View {
        val view = inflater.inflate(this.layout, parent, false)
        val diffView = view.findViewById(R.id.diff) as ImageView
        val nameView = view.findViewById(R.id.name) as TextView
        val descrView = view.findViewById(R.id.capital) as TextView
        val task = tasks.get(position)
        diffView.setImageResource(task.getImgToShow())
        nameView.setText(task.getTitle())
        descrView.setText(task.getDescr())
        return view
    }
}