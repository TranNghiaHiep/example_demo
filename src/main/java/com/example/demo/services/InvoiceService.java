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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public Page<Invoice> invoices(
        String keyword,
        Integer pageNumber,
        Integer pageSize,
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

        Pageable pageable = null;
        if (pageNumber != null && pageNumber != 0 && pageSize != null && pageSize !=0) {
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        return invoiceRepository.getInvoices(keyword, pageable, sortColumns, dateFilter, totalFilter, numberItemFilter);
    }

    public void save(Invoice entry) {
        invoiceRepository.save(entry);
    }

    public void save(List<Invoice> invoices) {
        invoiceRepository.save(invoices);
    }
}
