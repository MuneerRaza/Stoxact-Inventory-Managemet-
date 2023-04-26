package com.example.project.Helper;

public class Log {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date = "";

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    Item item = new Item("","",0,0,0);

    public Log() {
    }

    public Log(String date, Item item) {
        this.item = item;
        this.date = date;
    }
}
