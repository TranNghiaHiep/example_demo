package com.example.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.models.Invoice;
import com.example.demo.models.Invoice.SortColumn;
import com.example.demo.payloads.filters.Filter;
import com.example.demo.repositories.InvoiceRepository;
import com.example.demo.utils.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> invoices(String keyword,
        List<String> sorts,
        List<String> dates,
        List<Long> totals,
        List<Integer> numberItems
    ) {
        Filter dateFilter = Helper.getDateFilter(dates);
        Filter totalFilter = Helper.getLongFilter(totals);
        Filter numberItemFilter = Helper.getIntegerFilter(numberItems);
        Map<SortColumn, String> sortColumns = new HashMap<>();

        sorts.forEach((sort) -> {
            if (sort != null && !sort.equals("")) {
                String[] sortItems = sort.split(",");
                try {
                    SortColumn sortColumn = SortColumn.valueOf(sortItems[0].toUpperCase());
                    sortColumns.put(sortColumn, sortItems.length > 1 ? sortItems[1] : "asc");
                } catch (Exception e) {
                }
            }
        });

        return invoiceRepository.getInvoices(keyword, sortColumns, dateFilter, totalFilter, numberItemFilter);
    }

    // public Pageable invoices(String keyword,
    //     int pageNumber,
    //     int pageSize,
    //     List<String> sorts,
    //     Filter dateFilter,
    //     Filter totalFilter,
    //     Filter numberItemFilter
    // ) {

    // }

    public void save(Invoice entry) {
        invoiceRepository.save(entry);
    }

    public void save(List<Invoice> invoices) {
        invoiceRepository.save(invoices);
    }
}
