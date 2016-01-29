package com.donnfelker.tasko.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donnfelker.tasko.R;
import com.donnfelker.tasko.TaskoApplication;
import com.donnfelker.tasko.adapters.RealmTasksAdapter;
import com.donnfelker.tasko.http.ForecastListener;
import com.donnfelker.tasko.http.ForecastService;
import com.donnfelker.tasko.models.Task;

import java.util.UUID;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.forecast.models.Forecast;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainFragment extends Fragment {

    @Inject ForecastService forecastService;
    @Bind(R.id.main_task_list) protected RealmRecyclerView rv;

    private Realm realm;

    private RealmChangeListener changeListener;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TaskoApplication)(getActivity().getApplication())).getComponent().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        realm = Realm.getDefaultInstance();

        forecastService.getForecastFor("12.2", "1234.23", new ForecastListener() {
            @Override
            public void onForecastLoaded(Forecast forecast) {

            }

            @Override
            public void onForecastFailed(Exception e) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmResults<Task> tasks = realm.where(Task.class).findAll();
        RealmTasksAdapter tasksAdapter = new RealmTasksAdapter(getContext(), tasks, true, true, taskClickListener);
        rv.setAdapter(tasksAdapter);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_main, menu);
    }

    RealmTasksAdapter.ITaskItemClickListener taskClickListener = new RealmTasksAdapter.ITaskItemClickListener() {
        @Override
        public void onTaskClick(View caller, Task task) {
            // Hide the fab
            getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
            NewTaskFragment f = NewTaskFragment.newInstance(task.getUuid());
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, f, NewTaskFragment.class.getSimpleName())
                    .addToBackStack(NewTaskFragment.class.getSimpleName())
                    .commit();
        }
    };
}
