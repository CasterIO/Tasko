package com.donnfelker.tasko.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donnfelker.tasko.R;


public class NewTaskFragment extends Fragment {

    public NewTaskFragment() {
        // Required empty public constructor
    }

    public static NewTaskFragment newInstance() {
        return new NewTaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_task, container, false);
    }


}
