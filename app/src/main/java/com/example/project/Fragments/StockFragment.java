package com.example.project.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Dashboard;
import com.example.project.Helper.Item;
import com.example.project.Helper.StockAdapter;
import com.example.project.R;

import java.util.ArrayList;

public class StockFragment extends Fragment {


    public StockFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.stockRecyclerView);
        AutoCompleteTextView actv = view.findViewById(R.id.stock_search);
        ArrayList<Item> items = Dashboard.userData.items;

        StockAdapter adapter = new StockAdapter(view.getContext(), items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Dashboard.userData.getItemNameList());
        actv.setAdapter(arrayAdapter);
        actv.setThreshold(1);

        actv.setOnItemClickListener((adapterView, view1, i, l) -> {
            recyclerView.scrollToPosition(items.indexOf(Dashboard.userData.getItem(actv.getText().toString().trim())));
            actv.setText("");
        });
        Button addItemBtn = view.findViewById(R.id.addItemBtn);
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dilogue_box);
        TextView dialogName = dialog.findViewById(R.id.dialougeName);
        EditText dialogQty = dialog.findViewById(R.id.dialougeQty);
        EditText dialogCP = dialog.findViewById(R.id.dialougeCP);
        EditText dialogSP = dialog.findViewById(R.id.dialougeSP);
        Button dialogBtn = dialog.findViewById(R.id.dialogBtn);
        addItemBtn.setOnClickListener(view12 -> {

            dialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialogName.getText().toString().equals("")) {
                        dialogName.setError("Enter Name");
                    } else if (dialogQty.getText().toString().equals("")) {
                        dialogQty.setError("Enter Quantity");
                    } else if (dialogCP.getText().toString().equals("")) {
                        dialogCP.setError("Enter Cost Price");
                    } else if(dialogSP.getText().toString().equals("")){
                        dialogSP.setError("Enter Selling Price");
                    } else{
                        Item item = new Item(
                                dialogName.getText().toString().trim(),
                                Integer.parseInt(dialogQty.getText().toString().trim()),
                                Long.parseLong(dialogSP.getText().toString().trim()),
                                Long.parseLong(dialogCP.getText().toString().trim())
                        );
                        String key = Dashboard.userData.addItem(item);
                        item.setId(key);
                        int x = adapter.ItemInserted(item);
                        recyclerView.scrollToPosition(x);
                        dialogName.setText("");
                        dialogQty.setText("");
                        dialogSP.setText("");
                        dialogCP.setText("");
                        dialog.dismiss();
                    }
                }
            });
            dialogName.requestFocus();
            dialog.show();
        });
        return view;
    }

}