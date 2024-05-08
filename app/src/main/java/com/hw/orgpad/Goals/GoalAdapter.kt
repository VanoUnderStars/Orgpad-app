package com.hw.orgpad.Goals

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hw.orgpad.R

class GoalAdapter:ArrayAdapter<Goal> {

    private val inflater:LayoutInflater
    private val layout:Int
    private val goals:MutableList<Goal>

    constructor(context:Context, resource:Int, tasks:MutableList<Goal>) : super(context, resource, tasks)
    {
        this.goals = tasks
        this.layout = resource
        this.inflater = LayoutInflater.from(context)
    }

    override fun getView(position:Int, convertView:View?, parent:ViewGroup?):View {
        val view = inflater.inflate(this.layout, parent, false)
        val diffView = view.findViewById(R.id.diff) as ImageView
        val nameView = view.findViewById(R.id.name) as TextView
        val descrView = view.findViewById(R.id.capital) as TextView
        val goal = goals.get(position)
        diffView.setImageResource(goal.getImgToShow())
        nameView.setText(goal.getTitle())
        descrView.setText(goal.getDescr())
        return view
    }
}