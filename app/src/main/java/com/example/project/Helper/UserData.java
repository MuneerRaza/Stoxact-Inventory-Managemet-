package com.example.project.Helper;

import java.util.ArrayList;

public class UserData {
    public ArrayList<Item> items;
    public ArrayList<Log> logs;
    DataBase dataBase;
    public UserData(String uname) {
        dataBase = new DataBase(uname);
        items = dataBase.getItemList();
        logs = dataBase.getLogsList();
    }

    public ArrayList<String> getItemNameList(){
        ArrayList<String> nameList = new ArrayList<>();
        for(Item item: items){
            nameList.add(item.getName());
        }
        return nameList;
    }

    public Item getItem(String name){
        for(Item item: items){
            if(item.getName().equals(name)){
                return item;
            }
        }
        return null;
    }
    public String addItem(Item item){
       String key = dataBase.addItem(item);
       refreshItem();
       return key;
    }
    public void addLog(Log log){
        dataBase.addLog(log);
        refreshLogs();
    }

    public void updateItem(Item item) {
        dataBase.updateItem(item);
        refreshItem();
    }
    public void deleteItem(Item item){
        dataBase.deleteItem(item);
        refreshItem();
    }
    public void refreshItem(){
        items = dataBase.getItemList();
    }
    public void refreshLogs(){
        logs = dataBase.getLogsList();
    }
}
