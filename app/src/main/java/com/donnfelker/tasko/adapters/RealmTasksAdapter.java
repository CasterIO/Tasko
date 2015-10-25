package com.donnfelker.tasko.adapters;

import android.content.Context;

import com.donnfelker.tasko.models.Task;

import io.realm.RealmResults;

public class RealmTasksAdapter extends RealmModelAdapter<Task> {
    public RealmTasksAdapter(Context context, RealmResults<Task> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
