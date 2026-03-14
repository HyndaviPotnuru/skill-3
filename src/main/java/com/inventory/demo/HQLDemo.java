package com.inventory.demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;
import com.inventory.loader.ProductDataLoader;

public class HQLDemo {

    public static void main(String[] args) {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {

            // Run this once to insert sample products
            // ProductDataLoader.loadSampleProducts(session);

            sortProductsByPriceAscending(session);
            sortProductsByPriceDescending(session);
            sortProductsByQuantityDescending(session);

            getFirstThreeProducts(session);
            getNextThreeProducts(session);

            countTotalProducts(session);
            countProductsInStock(session);

            countProductsByDescription(session);

            findMinMaxPrice(session);

            groupProductsByDescription(session);

            filterProductsByPriceRange(session, 20.0, 100.0);

            findProductsStartingWith(session, "D");
            findProductsEndingWith(session, "p");
            findProductsContaining(session, "Desk");

            findProductsByNameLength(session, 5);

        } finally {
            session.close();
            factory.close();
        }
    }

    // TASK 3a
    public static void sortProductsByPriceAscending(Session session) {

        String hql = "FROM Product p ORDER BY p.price ASC";

        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\nProducts sorted by price ASC");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // TASK 3b
    public static void sortProductsByPriceDescending(Session session) {

        String hql = "FROM Product p ORDER BY p.price DESC";

        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\nProducts sorted by price DESC");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // TASK 4
    public static void sortProductsByQuantityDescending(Session session) {

        String hql = "FROM Product p ORDER BY p.quantity DESC";

        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\nProducts sorted by quantity DESC");

        for (Product p : products) {
            System.out.println(p.getName() + " Quantity: " + p.getQuantity());
        }
    }

    // TASK 5a
    public static void getFirstThreeProducts(Session session) {

        Query<Product> query = session.createQuery("FROM Product", Product.class);

        query.setFirstResult(0);
        query.setMaxResults(3);

        List<Product> products = query.list();

        System.out.println("\nFirst 3 products");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // TASK 5b
    public static void getNextThreeProducts(Session session) {

        Query<Product> query = session.createQuery("FROM Product", Product.class);

        query.setFirstResult(3);
        query.setMaxResults(3);

        List<Product> products = query.list();

        System.out.println("\nNext 3 products");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // TASK 6a
    public static void countTotalProducts(Session session) {

        Long count = session.createQuery(
                "SELECT COUNT(p) FROM Product p", Long.class).uniqueResult();

        System.out.println("\nTotal Products: " + count);
    }

    // TASK 6b
    public static void countProductsInStock(Session session) {

        Long count = session.createQuery(
                "SELECT COUNT(p) FROM Product p WHERE p.quantity > 0",
                Long.class).uniqueResult();

        System.out.println("\nProducts with quantity > 0 : " + count);
    }

    // TASK 6c
    public static void countProductsByDescription(Session session) {

        String hql = "SELECT p.description, COUNT(p) FROM Product p GROUP BY p.description";

        Query<Object[]> query = session.createQuery(hql, Object[].class);
        List<Object[]> results = query.list();

        System.out.println("\nProducts grouped by description");

        for (Object[] row : results) {

            System.out.println(row[0] + " : " + row[1]);
        }
    }

    // TASK 6d
    public static void findMinMaxPrice(Session session) {

        String hql = "SELECT MIN(p.price), MAX(p.price) FROM Product p";

        Object[] result = session.createQuery(hql, Object[].class).uniqueResult();

        System.out.println("\nMinimum Price : " + result[0]);
        System.out.println("Maximum Price : " + result[1]);
    }

    // TASK 7
    public static void groupProductsByDescription(Session session) {

        String hql = "SELECT p.description, p.name, p.price FROM Product p ORDER BY p.description";

        Query<Object[]> query = session.createQuery(hql, Object[].class);

        List<Object[]> results = query.list();

        System.out.println("\nProducts grouped by description");

        String current = "";

        for (Object[] row : results) {

            String description = (String) row[0];

            if (!description.equals(current)) {
                System.out.println("\n" + description);
                current = description;
            }

            System.out.println(row[1] + " $" + row[2]);
        }
    }

    // TASK 8
    public static void filterProductsByPriceRange(Session session, double min, double max) {

        String hql = "FROM Product p WHERE p.price BETWEEN :min AND :max";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setParameter("min", min);
        query.setParameter("max", max);

        List<Product> products = query.list();

        System.out.println("\nProducts between " + min + " and " + max);

        for (Product p : products) {
            System.out.println(p.getName() + " $" + p.getName());
        }
    }

    // TASK 9a
    public static void findProductsStartingWith(Session session, String prefix) {

        Query<Product> query = session.createQuery(
                "FROM Product p WHERE p.name LIKE :pattern",
                Product.class);

        query.setParameter("pattern", prefix + "%");

        List<Product> products = query.list();

        System.out.println("\nProducts starting with " + prefix);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }

    // TASK 9b
    public static void findProductsEndingWith(Session session, String suffix) {

        Query<Product> query = session.createQuery(
                "FROM Product p WHERE p.name LIKE :pattern",
                Product.class);

        query.setParameter("pattern", "%" + suffix);

        List<Product> products = query.list();

        System.out.println("\nProducts ending with " + suffix);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }

    // TASK 9c
    public static void findProductsContaining(Session session, String text) {

        Query<Product> query = session.createQuery(
                "FROM Product p WHERE p.name LIKE :pattern",
                Product.class);

        query.setParameter("pattern", "%" + text + "%");

        List<Product> products = query.list();

        System.out.println("\nProducts containing " + text);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }

    // TASK 9d
    public static void findProductsByNameLength(Session session, int length) {

        Query<Product> query = session.createQuery(
                "FROM Product p WHERE LENGTH(p.name)=:len",
                Product.class);

        query.setParameter("len", length);

        List<Product> products = query.list();

        System.out.println("\nProducts with name length " + length);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }

}