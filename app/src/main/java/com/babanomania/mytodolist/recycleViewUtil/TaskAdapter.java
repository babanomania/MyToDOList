package com.babanomania.mytodolist.recycleViewUtil;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.models.TaskBean;
import com.babanomania.mytodolist.persistance.TaskManager;

import java.util.List;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<TaskBean> tasksList;
    private TaskManager taskManager = TaskManager.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, desc, date;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            desc = (TextView) view.findViewById(R.id.desc);
            date = (TextView) view.findViewById(R.id.date);
        }

        public void setSelected() {
            itemView.setBackgroundColor(Color.parseColor("gray"));
        }

        public void setUnSelected() {
            super.itemView.setBackgroundColor(Color.parseColor("white"));
        }
    }

    public TaskAdapter(List<TaskBean> pTasksList) {
        this.tasksList = pTasksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TaskBean task = tasksList.get(position);
        holder.title.setText(task.getTitle());
        holder.desc.setText(task.getDescription());
        holder.date.setText(task.getDate());

        if( taskManager.isSelected(task) ){

            holder.setSelected();
            Log.d( "MyTODOList", "SetSelected - " + task.getTitle() );

        } else {

            holder.setUnSelected();
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

}
