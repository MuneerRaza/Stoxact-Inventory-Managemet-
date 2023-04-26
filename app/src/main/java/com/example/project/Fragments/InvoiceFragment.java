package com.example.project.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Dashboard;
import com.example.project.Helper.InvoiceAdapter;
import com.example.project.Helper.Item;
import com.example.project.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InvoiceFragment extends Fragment {

    public InvoiceFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.invoiceRecyclerView);
        AutoCompleteTextView actv = view.findViewById(R.id.invoice_search);
        ArrayList<Item> items = new ArrayList<>();

        InvoiceAdapter adapter = new InvoiceAdapter(view.getContext(), items, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        ArrayList<String> names = new ArrayList<>(Dashboard.userData.getItemNameList());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, names);
        actv.setAdapter(arrayAdapter);
        actv.setThreshold(1);

        actv.setOnItemClickListener((adapterView, view1, i, l) -> {
            Item item = Dashboard.userData.getItem(actv.getText().toString());
            if(item != null){
                items.add(new Item(item.getId(), item.getName(), 1, item.getSelling_price(), item.getCost_price()));
                actv.setText("");
                adapter.notifyItemInserted(items.size() - 1);
                recyclerView.scrollToPosition(items.size() - 1);
            } else{
                Toast.makeText(view.getContext(), "Item not found", Toast.LENGTH_SHORT).show();
            }

        });
        Button purchaseBtn = view.findViewById(R.id.confirm_purchase);
        purchaseBtn.setOnClickListener(view12 -> {
            purchaseBtn.setEnabled(false);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("E, MMM dd yyyy")).trim();

            for (Item x: items) {
                Item si = Dashboard.userData.getItem(x.getName());
                int siqty = si.getQty();
                System.out.println(siqty);
                int qty = siqty-x.getQty();
                System.out.println(qty);
//                if(qty == 0){
//                    Dashboard.userData.deleteItem(si);
//                }else{
//                    si.setQty(qty);
//                    Dashboard.userData.updateItem(si);
//                }
//                Log log = new Log(date, x);
//                Dashboard.userData.addLog(log);
            }
//            items.clear();
//            adapter.notifyItemRemoved(0);
            TextView total = view.findViewById(R.id.total);
            total.setText("Total: 0 Rs");
            purchaseBtn.setEnabled(true);
        });
        return view;
    }
}