package com.babanomania.mytodolist.recycleViewUtil;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.models.TaskBean;

import java.util.List;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<TaskBean> tasksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, date;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            desc = (TextView) view.findViewById(R.id.desc);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    public TaskAdapter(List<TaskBean> pMoviesList) {
        this.tasksList = pMoviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TaskBean task = tasksList.get(position);
        holder.title.setText(task.getTitle());
        holder.desc.setText(task.getDescription());
        holder.date.setText(task.getDate());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
