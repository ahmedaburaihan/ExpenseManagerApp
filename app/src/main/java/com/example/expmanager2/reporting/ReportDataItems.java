package com.example.expmanager2.reporting;

public class ReportDataItems {
    String itemName, itemDesc, itemDate;
    int itemAmount;

    public ReportDataItems(String itemName, String itemDesc, String itemDate, int itemAmount) {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemDate = itemDate;
        this.itemAmount = itemAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }
}
