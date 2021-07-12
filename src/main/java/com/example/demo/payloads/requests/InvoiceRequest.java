package com.example.demo.payloads.requests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceRequest {
    private long total;
    private Date date;
    private String customer;
    private String note;
    private List<ItemRequest> items = new ArrayList<>();

    public List<ItemRequest> getItems() {
        return this.items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomer() {
        return this.customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
