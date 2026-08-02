package com.project.code.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "order_details")
public class OrderDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonManagedReference("order-customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @JsonManagedReference
    private Store store;

    @NotNull
    private Double totalPrice;

    @NotNull
    private LocalDateTime date;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    //Constructors
    
    // Required by JPA/Hibernate
    public OrderDetails() {
    }

    // Used when creating a new order
    public OrderDetails(
            Customer customer,
            Store store,
            Double totalPrice,
            LocalDateTime date
    ) {
        this.customer = customer;
        this.store = store;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    //Getters and setters
    public long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Store getStore() {
        return store;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }



// 1. Add 'id' field:
//    - Type: private Long 
//    - This field will be auto-incremented.
//    - Use @Id to mark it as the primary key.
//    - Use @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.

// 2. Add 'customer' field:
//    - Type: private Customer
//    - This field refers to the customer who placed the order.
//    - Use @ManyToOne with @JoinColumn(name = "customer_id") to define the foreign key relationship.
//    - Apply @JsonManagedReference to handle bidirectional relationships and JSON serialization.

// 3. Add 'store' field:
//    - Type: private Store
//    - This field refers to the store from where the order was placed.
//    - Use @ManyToOne with @JoinColumn(name = "store_id") to define the foreign key relationship.
//    - Apply @JsonManagedReference to handle bidirectional relationships and JSON serialization.

// 4. Add 'totalPrice' field:
//    - Type: private Double
//    - This field represents the total price of the order.

// 5. Add 'date' field:
//    - Type: private LocalDateTime
//    - This field represents the date and time when the order was placed.

// 6. Add 'orderItems' field:
//    - Type: private List<OrderItem>
//    - This field represents the list of items in the order.
//    - Use @OneToMany(mappedBy = "order", fetch = FetchType.EAGER) to establish the one-to-many relationship with OrderItem.
//    - Apply @JsonManagedReference to prevent circular references during JSON serialization.

// 7. Add constructors:
//    - A no-argument constructor.
//    - A parameterized constructor that accepts Customer, Store, totalPrice, and date as parameters.

// 8. Add @Entity annotation:
//    - Use @Entity above the class name to mark it as a JPA entity.

// 9. Add Getters and Setters:
//    - Add getter and setter methods for all fields (id, customer, store, totalPrice, date, orderItems).
  
}
