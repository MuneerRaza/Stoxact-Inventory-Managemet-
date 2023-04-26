package com.example.project.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Dashboard;
import com.example.project.Helper.Log;
import com.example.project.Helper.LogsAdapter;
import com.example.project.R;

import java.util.ArrayList;

public class LogsFragment extends Fragment {

    public LogsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.logsRecyclerView);
        AutoCompleteTextView actv = view.findViewById(R.id.logs_search);
        ArrayList<Log> logs = Dashboard.userData.logs;

        LogsAdapter adapter = new LogsAdapter(view.getContext(), logs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Dashboard.userData.getItemNameList());
        actv.setAdapter(arrayAdapter);
        actv.setThreshold(1);

        actv.setOnItemClickListener((adapterView, view1, i, l) -> {
            boolean flag = false;
            for (int j = 0; j < logs.size(); j++) {
                if (logs.get(j).getItem().getName().equals(actv.getText().toString())) {
                    recyclerView.scrollToPosition(j);
                    flag = true;
                }
            }
            if(!flag){
                Toast.makeText(view.getContext(), "Item not in Logs", Toast.LENGTH_SHORT).show();
            }

            actv.setText("");

        });

        return view;
    }
}