package com.example.project.Helper;

public class Item {
    private String name, id;
    private int qty;
    private long selling_price, cost_price;

    public Item(String id, String name, int qty, long selling_price, long cost_price) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.selling_price = selling_price;
        this.cost_price = cost_price;
    }
    public Item(String name, int qty, long selling_price, long cost_price) {
        this.id = "";
        this.name = name;
        this.qty = qty;
        this.selling_price = selling_price;
        this.cost_price = cost_price;
    }
    public Item(){
        this.name = "name";
        this.qty = 0;
        this.selling_price = 0;
        this.cost_price = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public long getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(long selling_price) {
        this.selling_price = selling_price;
    }

    public long getCost_price() {
        return cost_price;
    }

    public void setCost_price(long cost_price) {
        this.cost_price = cost_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
