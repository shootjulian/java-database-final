package com.project.code.Model;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String address;

    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
    @JsonManagedReference("inventory-store")
    private List<Inventory> inventories = new ArrayList<>();

    //Constructors
    // Required by JPA/Hibernate
    public Store() {
    }

    // Used when creating a new store
    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    //Getters and setters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }



// 1. Add 'id' field:
//    - Type: private long 
//    - This field will be auto-incremented.
//    - Use @Id to mark it as the primary key.
//    - Use @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.

// 2. Add 'name' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

// 3. Add 'address' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull and @NotBlank annotations to enforce this rule.

// 4. Add relationships:
//    - **Inventory**: A store can have multiple inventory entries.
//    - Use @OneToMany(mappedBy = "store") to reflect the one-to-many relationship with Inventory.
//    - Use @JsonManagedReference("inventory-store") to manage bidirectional relationships and avoid circular references.

// 5. Add constructor:
//    - Create a constructor that accepts name and address as parameters to initialize the Store object.

// 6. Add @Entity annotation:
//    - Use @Entity above the class name to mark it as a JPA entity.

// 7. Add Getters and Setters:
//    - Add getter and setter methods for all fields (id, name, address).

}

