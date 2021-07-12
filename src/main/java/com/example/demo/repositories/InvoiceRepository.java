package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.demo.models.Invoice;
import com.example.demo.models.Invoice.SortColumn;
import com.example.demo.payloads.filters.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class InvoiceRepository {
    @Autowired
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    public List<Invoice> getInvoices(String keyword,
        Map<SortColumn, String> sorts,
        Filter dateFilter,
        Filter totalFilter,
        Filter numberItemFilter
    ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        // Join<Invoice, Item> secondTable = root.join("items", JoinType.LEFT);
        // query
        query.select(root);
        List<Predicate> predicates = new ArrayList<Predicate>();

        // dateFilter
        if (dateFilter.getMin() != null && dateFilter.getMax() != null) {
            predicates.add(
                builder.between(
                    root.get("date"),
                    (Date) dateFilter.getMin(),
                    (Date) dateFilter.getMax()
                )
            );
        } else if (dateFilter.getMin() != null) {
            predicates.add(
                builder.between(
                    root.get("date"),
                    (Date) dateFilter.getMin(),
                    new Date()
                )
            );
        }

        // totalFilter
        if (totalFilter.getMin() != null && totalFilter.getMax() != null) {
            predicates.add(
                builder.and(
                    builder.ge(root.get("total"), (Long) totalFilter.getMin()),
                    builder.le(root.get("total"), (Long) totalFilter.getMax())
                )
            );
        } else if (totalFilter.getMin() != null) {
            predicates.add(
                builder.ge(root.get("total"), (Long) totalFilter.getMin())
            );
        }

        // numberItemFilter
        if (numberItemFilter.getMin() != null && numberItemFilter.getMax() != null) {
            predicates.add(
                builder.between(builder.size(root.get("items")), (Integer) numberItemFilter.getMin(), (Integer) numberItemFilter.getMax())
            );
        } else if (numberItemFilter.getMin() != null) {
            predicates.add(builder.ge(builder.size(root.get("items")), (Integer) numberItemFilter.getMin()));
        }

        query.where(predicates.toArray(new Predicate[] {}));

        // sorts
        List<Order> orders = sorts.entrySet().stream().map((entry) -> {
            if (entry.getKey() == SortColumn.ITEM_NUMBER) {
                if (entry.getValue() != null && entry.getValue().equals("asc")) {
                    return builder.asc(builder.size(root.get("items")));
                } else {
                    return builder.desc(builder.size(root.get("items")));
                }
            } else {
                if (entry.getValue() != null && entry.getValue().equals("asc")) {
                    return builder.asc(root.get(entry.getKey().toString()));
                } else {
                    return builder.desc(root.get(entry.getKey().toString()));
                }
            }
        }).collect(Collectors.toList());

        query.orderBy(orders);

        List<Invoice> invoices = em.createQuery(query).getResultList();

        return invoices;
    }

    // public Pageable getInvoices(
    //     int pageNumber,
    //     int pageSize,
    //     String keyword,
    //     Map<SortColumn, String> sorts,
    //     Filter dateFilter,
    //     Filter totalFilter,
    //     Filter numberItemFilter
    // ) {
    //     CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

    //     CriteriaQuery<Long> countQuery = criteriaBuilder
    //     .createQuery(Long.class);
    //     countQuery.select(criteriaBuilder
    //     .count(countQuery.from(Invoice.class)));


    //     Long count = entityManager.createQuery(countQuery)
    //     .getSingleResult();

    //     CriteriaQuery<Foo> criteriaQuery = criteriaBuilder
    //     .createQuery(Foo.class);
    //     Root<Foo> from = criteriaQuery.from(Foo.class);
    //     CriteriaQuery<Foo> select = criteriaQuery.select(from);

    //     TypedQuery<Foo> typedQuery = entityManager.createQuery(select);
    //     while (pageNumber < count.intValue()) {
    //         typedQuery.setFirstResult(pageNumber - 1);
    //         typedQuery.setMaxResults(pageSize);
    //         System.out.println("Current page: " + typedQuery.getResultList());
    //         pageNumber += pageSize;
    //     }
    // }

    public void save(Invoice entry) {
        em.persist(entry);
    }

    @Transactional
    public void save(List<Invoice> invoices) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        for (int i = 0; i < invoices.size(); i++) {
            entityManager.persist(invoices.get(i));

            if ((i % 10000) == 0) {
                entityManager.getTransaction().commit();
                entityManager.clear();          
                entityManager.getTransaction().begin();
            }
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
