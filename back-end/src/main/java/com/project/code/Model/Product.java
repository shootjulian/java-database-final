package com.project.code.Model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "products",
    uniqueConstraints = @UniqueConstraint(columnNames = "sku")
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Category cannot be null")
    private String category;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "SKU cannot be null")
    private String sku;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference("inventory-product")
    private List<Inventory> inventories = new ArrayList<>();


    // Constructors
    public Product() {
    }

    public Product(
            String name,
            String category,
            Double price,
            String sku
    ) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.sku = sku;
    }


    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }


    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}
// 1. Add 'id' field:
//    - Type: private long 
//    - This field will be auto-incremented.
//    - Use @Id to mark it as the primary key.
//    - Use @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.

// 2. Add 'name' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

// 3. Add 'category' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

// 4. Add 'price' field:
//    - Type: private Double
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

// 5. Add 'sku' field:
//    - Type: private String
//    - This field cannot be empty, must be unique, use the @NotNull annotation to enforce this rule.
//    - Use the @Table annotation with uniqueConstraints to ensure the 'sku' column is unique.

//    Example: @Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))

// 6. Add relationships:
//    - **Inventory**: A product can have multiple inventory entries.
//    - Use @OneToMany(mappedBy = "product") to reflect the one-to-many relationship with Inventory.
//    - Use @JsonManagedReference("inventory-product") to manage bidirectional relationships and avoid circular references.

// 7. Add @Entity annotation:
//    - Use @Entity above the class name to mark it as a JPA entity.

// 8. Add Getters and Setters:
//    - Add getter and setter methods for all fields (id, name, category, price, sku).



