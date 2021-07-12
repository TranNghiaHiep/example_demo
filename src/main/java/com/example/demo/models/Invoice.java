package com.example.demo.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.demo.utils.RandomHelper;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "Invoices")
public class Invoice {
    @Id
    @Type(type = "uuid-char")
    private UUID uuid = UUID.randomUUID();;

    private long total;
    private Date date;
    private String customer;

    @Column(name = "note", columnDefinition = "nvarchar(225)")
    private String note;

    public Invoice() {}

    public Invoice(long total, String customer, Date date, String note) {
        this.total = total;
        this.date = date;
        this.customer = customer;
        this.note = note;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Item> items = new ArrayList<>();

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        item.setInvoice(this);
        this.items.add(item);
    }

    public UUID getUuid() {
        return this.uuid;
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

    public static Invoice random() {
        Invoice invoice = new Invoice();
        invoice.total = (long) Math.random();
        invoice.date = new Date();
        invoice.customer = RandomHelper.getAlphaNumericString(30);

        for (int i = 0; i < RandomHelper.getNumberic(3, 50); i++) {
            invoice.addItem(Item.random());
        }
        
        return invoice;
    }

    public enum SortColumn {
        TOTAL("total"),
        DATE("date"),
        ITEM_NUMBER("item_number");

        private String value;

        SortColumn(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }
}
