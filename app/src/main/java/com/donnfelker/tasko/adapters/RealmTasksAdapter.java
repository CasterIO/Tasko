package com.donnfelker.tasko.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.donnfelker.tasko.R;
import com.donnfelker.tasko.models.Task;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class RealmTasksAdapter extends RealmBasedRecyclerViewAdapter<Task, RealmTasksAdapter.ViewHolder> {

    public RealmTasksAdapter(Context context, RealmResults<Task> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.task_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Task task = realmResults.get(position);
        viewHolder.taskName.setText(task.getName());
    }

    class ViewHolder extends RealmViewHolder {

        @Bind(R.id.task_item_task_name) public TextView taskName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
