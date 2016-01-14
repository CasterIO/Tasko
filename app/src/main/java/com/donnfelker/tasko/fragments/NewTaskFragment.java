package com.donnfelker.tasko.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class NewTaskFragment extends BaseFragment {

    private static final String TASK_ID = "task_id";

    @Bind(R.id.new_task_add) Button actionButton;
    @Bind(R.id.new_task_task_name) EditText taskName;
    @Bind(R.id.new_task_task_desc) EditText taskDesc;
    @Bind(R.id.new_task_task_name_input_layout) TextInputLayout nameInputLayout;

    private Realm realm;
    private Task taskToEdit;

    public NewTaskFragment() {
        // Required empty public constructor
    }

    public static NewTaskFragment newInstance() {
        return new NewTaskFragment();
    }

    public static NewTaskFragment newInstance(String taskId) {
        NewTaskFragment f = new NewTaskFragment();
        Bundle args = new Bundle();
        args.putString(TASK_ID, taskId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_task, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null && getArguments().getString(TASK_ID) != null) {
            // We need to go into edit mode.
            final String id = getArguments().getString(TASK_ID);
            taskToEdit = realm.where(Task.class).equalTo("uuid", id).findFirst();

            // Load task info into UI
            taskName.setText(taskToEdit.getName());
            taskDesc.setText(taskToEdit.getDescription());

            // Change UI
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit);
            actionButton.setText(R.string.save);
        }
    }

    @OnClick(R.id.new_task_add)
    public void onAddClick(View v) {
        if(taskToEdit == null) {
            addTask();
        } else {
            editTask();
        }
    }

    private void editTask() {
        if (nameIsValid()) return;

        realm.beginTransaction();
        taskToEdit.setName(taskName.getText().toString());
        taskToEdit.setDescription(taskDesc.getText().toString());
        realm.commitTransaction();

        if(getView() != null) {
            Snackbar.make(getView(), "Task Updated", Snackbar.LENGTH_LONG).show();
        }

        getFragmentManager().popBackStack();
    }

    private void addTask() {
        if (nameIsValid()) return;

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

    private boolean nameIsValid() {
        if(Strings.isEmpty(taskName.getText())) {
            nameInputLayout.setErrorEnabled(true);
            nameInputLayout.setError(getString(R.string.error_new_task_name_required));
            return true;
        } else {
            nameInputLayout.setErrorEnabled(true);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
