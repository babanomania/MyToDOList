package com.babanomania.mytodolist.core;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.models.Task;
import com.babanomania.mytodolist.persistance.TaskManager;
import com.babanomania.mytodolist.util.EntityUtil;

import java.util.List;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<Task> tasksList;
    private TaskManager taskManager = TaskManager.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, label, date;
        public View rowView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            label = (TextView) view.findViewById(R.id.labels);
            date = (TextView) view.findViewById(R.id.date);
            this.rowView = view;
        }

    }

    public MainAdapter(List<Task> pTasksList) {
        this.tasksList = pTasksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Task task = tasksList.get(position);
        holder.title.setText(task.getTitle());
        holder.label.setText( EntityUtil.getHashedLabels(task) );
        holder.date.setText( EntityUtil.getStringFromDate(task.getDate()));

        if( taskManager.isSelected(task) ){

            holder.rowView.setBackgroundColor(Color.parseColor("GRAY"));
            holder.rowView.setSelected(true);
            Log.d( "MyTODOList", "SetSelected - " + task.getTitle() );

        } else {

            holder.rowView.setBackgroundColor(Color.parseColor("WHITE"));
            holder.rowView.setSelected(false);
            Log.d( "MyTODOList", "NOT-SetSelected - " + task.getTitle() );
        }

    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public void removeSelection(){
        taskManager.clearSelection();
        notifyDataSetChanged();
    }

    public void selectAllTasks(Context context){
        taskManager.filterData(context, TaskManager.FilterByCal.all);
        notifyDataSetChanged();
    }

}
