package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.configs.V1APIController;
import com.example.demo.models.Invoice;
import com.example.demo.models.Item;
import com.example.demo.payloads.requests.InvoiceRequest;
import com.example.demo.services.InvoiceService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@V1APIController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    public ResponseEntity<?> invoices(@RequestParam(name = "date", required = false) List<String> dates,
        @RequestParam(name = "total", required = false) List<Long> totals,
        @RequestParam(name = "numberItem", required = false) List<Integer> numberItems,
        @RequestParam(name = "sort", required = false) List<String> sorts,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer pageSize,
        @RequestParam(required = false) Integer pageNumber
    ) {
        Page<Invoice> invoices = this.invoiceService.invoices(keyword, pageNumber, pageSize, sorts, dates, totals, numberItems);
        return ResponseEntity.ok().body(invoices);
    }

    @PostMapping("")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) {

        Invoice invoice = new Invoice(request.getTotal(), request.getCustomer(), request.getDate(), request.getNote());
        
        request.getItems().forEach((item) -> {
            invoice.addItem(new Item(item.getName(), item.getPrice(), item.getNote()));
        });

        invoiceService.save(invoice);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/random")
    public ResponseEntity<?> randomCreate() {
        List<Invoice> invoices = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            invoices.add(Invoice.random());
        }

        invoiceService.save(invoices);
        return ResponseEntity.ok().build();
    }
}
