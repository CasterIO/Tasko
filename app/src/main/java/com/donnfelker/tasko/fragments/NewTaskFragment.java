package com.donnfelker.tasko.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.donnfelker.tasko.R;
import com.donnfelker.tasko.Strings;
import com.donnfelker.tasko.models.Task;

import java.util.Calendar;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class NewTaskFragment extends BaseFragment {

    @Bind(R.id.new_task_task_name) EditText taskName;
    @Bind(R.id.new_task_task_desc) EditText taskDesc;
    @Bind(R.id.new_task_task_name_input_layout) TextInputLayout nameInputLayout;

    private Realm realm;

    public NewTaskFragment() {
        // Required empty public constructor
    }

    public static NewTaskFragment newInstance() {
        return new NewTaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getContext()).build();

        // Clear the realm from last time
        Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        realm = Realm.getInstance(realmConfiguration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_task, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.new_task_add)
    public void onAddClick(View v) {

        if(Strings.isEmpty(taskName.getText())) {
            nameInputLayout.setErrorEnabled(true);
            nameInputLayout.setError(getString(R.string.error_new_task_name_required));
            return;
        } else {
            nameInputLayout.setErrorEnabled(true);
        }

        realm = Realm.getInstance(getActivity());

        realm.beginTransaction();

        Task task = realm.createObject(Task.class);

        task.setUuid(UUID.randomUUID().toString());
        task.setName(taskName.getText().toString());
        task.setDescription(taskDesc.getText().toString());
        task.setTimestamp(Calendar.getInstance().getTime());

        realm.commitTransaction();

        if(getView() != null) {
            Snackbar.make(getView(), "Task Added", Snackbar.LENGTH_LONG).show();
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
