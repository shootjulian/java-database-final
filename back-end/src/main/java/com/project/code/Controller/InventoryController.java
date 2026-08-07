package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;


    // UPDATE INVENTORY AND PRODUCT
    @PutMapping
    public Map<String, String> updateInventory(
            @RequestBody CombinedRequest request
    ) {

        Map<String, String> response = new HashMap<>();

        try {

            Product product = request.getProduct();
            Inventory inventory = request.getInventory();

            // Does this product exist?
            if (!serviceClass.validateProductId(product.getId())) {

                response.put(
                        "message",
                        "Product not present in database"
                );

                return response;
            }

            // Search actual inventory stored in DB
            Inventory existingInventory =
                    inventoryRepository.findByProductIdAndStoreId(
                            inventory.getProduct().getId(),
                            inventory.getStore().getId()
                    );

            if (existingInventory == null) {

                response.put(
                        "message",
                        "No data available"
                );

                return response;
            }

            // Update product information
            productRepository.save(product);

            // Update stock
            existingInventory.setStockLevel(
                    inventory.getStockLevel()
            );

            inventoryRepository.save(existingInventory);

            response.put(
                    "message",
                    "Successfully updated product"
            );

        } catch (DataIntegrityViolationException e) {

            response.put(
                    "message",
                    "Data integrity error"
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }


    // CREATE INVENTORY
    @PostMapping
    public Map<String, String> saveInventory(
            @RequestBody Inventory inventory
    ) {

        Map<String, String> response = new HashMap<>();

        try {

            if (serviceClass.validateInventory(inventory)) {

                inventoryRepository.save(inventory);

                response.put(
                        "message",
                        "data saved successfully"
                );

            } else {

                response.put(
                        "message",
                        "data already present"
                );
            }

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }


    // GET ALL PRODUCTS FROM ONE STORE
    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(
            @PathVariable Long storeid
    ) {

        Map<String, Object> response = new HashMap<>();

        List<Inventory> inventories =
                inventoryRepository.findByStore_Id(storeid);

        List<Product> products =
                inventories.stream()
                        .map(Inventory::getProduct)
                        .toList();

        response.put(
                "products",
                products
        );

        return response;
    }


    // FILTER PRODUCTS IN A STORE
    @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(
            @PathVariable String category,
            @PathVariable String name,
            @PathVariable Long storeid
    ) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products;


        // No category and no name
        if (category.equalsIgnoreCase("null")
                && name.equalsIgnoreCase("null")) {

            products =
                    inventoryRepository
                            .findByStore_Id(storeid)
                            .stream()
                            .map(Inventory::getProduct)
                            .toList();
        }


        // Search only by name
        else if (category.equalsIgnoreCase("null")) {

            products =
                    productRepository.findByNameLike(
                            storeid,
                            name
                    );
        }


        // Search only by category
        else if (name.equalsIgnoreCase("null")) {

            products =
                    inventoryRepository
                            .findByStore_Id(storeid)
                            .stream()
                            .map(Inventory::getProduct)
                            .filter(product ->
                                    product.getCategory()
                                            .equalsIgnoreCase(category)
                            )
                            .toList();
        }


        // Search by category + name
        else {

            products =
                    productRepository
                            .findByNameLike(storeid, name)
                            .stream()
                            .filter(product ->
                                    product.getCategory()
                                            .equalsIgnoreCase(category)
                            )
                            .toList();
        }


        response.put(
                "product",
                products
        );

        return response;
    }


    // SEARCH PRODUCT NAME IN A STORE
    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(
            @PathVariable String name,
            @PathVariable Long storeId
    ) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products =
                productRepository.findByNameLike(
                        storeId,
                        name
                );

        response.put(
                "product",
                products
        );

        return response;
    }


    // REMOVE PRODUCT FROM INVENTORY
    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(
            @PathVariable Long id
    ) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.validateProductId(id)) {

            response.put(
                    "message",
                    "Product not present in database"
            );

            return response;
        }

        inventoryRepository.deleteByProductId(id);

        response.put(
                "message",
                "Product deleted successfully"
        );

        return response;
    }


    // CHECK STOCK
    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(
            @PathVariable Integer quantity,
            @PathVariable Long storeId,
            @PathVariable Long productId
    ) {

        Inventory inventory =
                inventoryRepository.findByProductIdAndStoreId(
                        productId,
                        storeId
                );

        if (inventory == null) {
            return false;
        }

        return inventory.getStockLevel() >= quantity;
    }
}