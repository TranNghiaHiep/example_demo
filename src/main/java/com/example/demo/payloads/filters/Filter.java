package com.example.demo.payloads.filters;

public class Filter {
    private Object min;
    private Object max;

    public Filter(Object min, Object max) {
        this.min = min;
        this.max = max;
    }

    public Filter() {
    }

    public Object getMin() {
        return this.min;
    }

    public void setMin(Object min) {
        this.min = min;
    }

    public Object getMax() {
        return this.max;
    }

    public void setMax(Object max) {
        this.max = max;
    }
}
