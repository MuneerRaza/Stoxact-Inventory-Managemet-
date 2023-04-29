package com.example.project.Helper;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBase {
    DatabaseReference stockData;
    DatabaseReference logsData;
    String username;
    public DataBase(String username) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        username = username.split("\\.")[0];
//        instance.setPersistenceEnabled(true);
        this.username = username;
        stockData = instance.getReference("stock").child(username);
        logsData = instance.getReference("logs").child(username);
//        stockData.keepSynced(true);
//        logsData.keepSynced(true);
    }
    public String addItem(Item item){
        assert item!=null;
        String key = stockData.push().getKey();
        if(key != null) {
            item.setId(key);
            stockData.child(key).setValue(item);
            return key;
        }
        return null;
    }
    public void addLog (Log log){
        String key = logsData.push().getKey();
        if(key != null) {
            logsData.child(key).setValue(log);
        }
    }

    public void updateItem(Item i){
        HashMap<String, Object> map = new HashMap<>();
        map.put("cost_price",i.getCost_price());
        map.put("id", i.getId());
        map.put("name", i.getName());
        map.put("qty", i.getQty());
        map.put("selling_price", i.getSelling_price());
        stockData.child(i.getId()).updateChildren(map);
    }
    public void deleteItem(Item item){
        stockData.child(item.getId()).removeValue();
    }
    public ArrayList<Log> getLogsList(){
        ArrayList<Log> logsList = new ArrayList<>();
        logsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    logsList.add(s.getValue(Log.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return logsList;
    }
    public ArrayList<Item> getItemList(){
        ArrayList<Item> itemList = new ArrayList<>();
        stockData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    Item i = s.getValue(Item.class);
                    if(i!=null){
                        itemList.add(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return itemList;
    }


}
