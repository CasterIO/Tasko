package com.donnfelker.tasko;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.donnfelker.tasko.fragments.MainFragment;
import com.donnfelker.tasko.fragments.NewTaskFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject SharedPreferences prefs;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TaskoApplication)getApplication()).getComponent().inject(this);
        if(prefs != null) {
            Log.d("Dagger2", "Prefs is not null");
        }

        String id = getIntent().getStringExtra("person_id");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setRippleColor(getResources().getColor(R.color.ripple_color));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNewTask();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        Log.d("Test", "test");

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                if(backStackCount == 0) {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

        navigateToMainFragment();

    }

    private void navigateToMainFragment() {
        MainFragment f = MainFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, f, MainFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigateToNewTask(MenuItem item) {
        navigateToNewTask();
    }

    private void navigateToNewTask() {
        fab.setVisibility(View.GONE);
        NewTaskFragment f = NewTaskFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, f, NewTaskFragment.class.getSimpleName())
                .addToBackStack(NewTaskFragment.class.getSimpleName())
                .commit();
    }
}
