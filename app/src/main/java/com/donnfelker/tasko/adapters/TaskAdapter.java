package com.donnfelker.tasko.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donnfelker.tasko.R;
import com.donnfelker.tasko.models.Task;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskAdapter extends RealmRecyclerViewAdapter<Task> {

    class TaskViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.task_item_task_name) public TextView taskName;

        public TaskViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TaskViewHolder tvh = (TaskViewHolder)holder;
        Task t = getItem(position);
        tvh.taskName.setText(t.getName());
    }

    /* The inner RealmBaseAdapter
     * view count is applied here.
     *
     * getRealmAdapter is defined in RealmRecyclerViewAdapter.
     */
    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}
