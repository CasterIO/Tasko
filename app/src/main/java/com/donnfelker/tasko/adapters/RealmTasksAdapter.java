package com.donnfelker.tasko.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donnfelker.tasko.R;
import com.donnfelker.tasko.models.Task;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class RealmTasksAdapter extends RealmBasedRecyclerViewAdapter<Task, RealmTasksAdapter.ViewHolder> {

    private ITaskItemClickListener clickListener;

    public RealmTasksAdapter(Context context, RealmResults<Task> realmResults, boolean automaticUpdate, boolean animateResults, ITaskItemClickListener clickListener) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.task_item, viewGroup, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Task task = realmResults.get(position);
        viewHolder.taskName.setText(task.getName());
    }

    class ViewHolder extends RealmViewHolder implements View.OnClickListener {

        @Bind(R.id.task_item_task_name) public TextView taskName;
        private ITaskItemClickListener clickListener;

        public ViewHolder(View view, ITaskItemClickListener clickListener) {
            super(view);
            this.clickListener = clickListener;
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null) {
                clickListener.onTaskClick(v, realmResults.get(getAdapterPosition()));
            }
        }
    }

    public static interface ITaskItemClickListener {
        void onTaskClick(View caller, Task task);
    }
}
