package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.payloads.filters.Filter;

public class Helper {
    public static Filter getDateFilter(List<String> dateList) {
        if (dateList.size() == 0) {
            return new Filter();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> dates = dateList.stream().map((dateString) -> {
            try {
                return sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        Date min = Collections.min(dates);
        Date max = Collections.max(dates);
        return new Filter(min, max == min ? null : max);
    }

    public static Filter getLongFilter(List<Long> longList) {
        if (longList.size() == 0) {
            return new Filter();
        }

        Long min = Collections.min(longList);
        Long max = Collections.max(longList);

        return new Filter(min, max == min ? null : max);
    }

    public static Filter getIntegerFilter(List<Integer> integerList) {
        if (integerList.size() == 0) {
            return new Filter();
        }

        Integer min = Collections.min(integerList);
        Integer max = Collections.max(integerList);

        return new Filter(min, max == min ? null : max);
    }
}
