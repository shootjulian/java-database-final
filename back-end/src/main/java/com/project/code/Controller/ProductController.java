package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;


    // ADD PRODUCT
    @PostMapping
    public Map<String, String> addProduct(
            @RequestBody Product product
    ) {

        Map<String, String> response = new HashMap<>();

        try {

            if (!serviceClass.validateProduct(product)) {

                response.put(
                        "message",
                        "Product already exists"
                );

                return response;
            }

            productRepository.save(product);

            response.put(
                    "message",
                    "Product saved successfully"
            );

        } catch (DataIntegrityViolationException e) {

            response.put(
                    "message",
                    "Product could not be saved. SKU may already exist."
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }


    // GET PRODUCT BY ID
    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(
            @PathVariable Long id
    ) {

        Map<String, Object> response = new HashMap<>();

        Product product =
                productRepository
                        .findById(id)
                        .orElse(null);

        response.put(
                "products",
                product
        );

        return response;
    }


    // UPDATE PRODUCT
    @PutMapping
    public Map<String, String> updateProduct(
            @RequestBody Product product
    ) {

        Map<String, String> response = new HashMap<>();

        try {

            productRepository.save(product);

            response.put(
                    "message",
                    "Product updated successfully"
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }


    // FILTER BY NAME AND/OR CATEGORY
    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(
            @PathVariable String name,
            @PathVariable String category
    ) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products;


        // Nothing specified
        if (name.equalsIgnoreCase("null")
                && category.equalsIgnoreCase("null")) {

            products =
                    productRepository.findAll();
        }


        // Only category
        else if (name.equalsIgnoreCase("null")) {

            products =
                    productRepository.findByCategory(
                            category
                    );
        }


        // Only name
        else if (category.equalsIgnoreCase("null")) {

            products =
                    productRepository
                            .findAll()
                            .stream()
                            .filter(product ->
                                    product.getName()
                                            .toLowerCase()
                                            .contains(
                                                    name.toLowerCase()
                                            )
                            )
                            .toList();
        }


        // Name AND category
        else {

            products =
                    productRepository
                            .findByCategory(category)
                            .stream()
                            .filter(product ->
                                    product.getName()
                                            .toLowerCase()
                                            .contains(
                                                    name.toLowerCase()
                                            )
                            )
                            .toList();
        }


        response.put(
                "products",
                products
        );

        return response;
    }


    // GET ALL PRODUCTS
    @GetMapping
    public Map<String, Object> listProduct() {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "products",
                productRepository.findAll()
        );

        return response;
    }


    // CATEGORY + STORE
    @GetMapping("filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(
            @PathVariable String category,
            @PathVariable Long storeid
    ) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products =
                inventoryRepository
                        .findByStore_Id(storeid)
                        .stream()
                        .map(Inventory::getProduct)
                        .filter(product ->
                                product.getCategory()
                                    .equalsIgnoreCase(category)
                        )
                        .toList();

        response.put(
                "product",
                products
        );

        return response;
    }


    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(
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

        // Delete FK records first
        inventoryRepository.deleteByProductId(id);

        // Delete Product
        productRepository.deleteById(id);

        response.put(
                "message",
                "Product deleted successfully"
        );

        return response;
    }


    // SEARCH PRODUCT BY PARTIAL NAME
    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(
            @PathVariable String name
    ) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products =
                productRepository
                        .findAll()
                        .stream()
                        .filter(product ->
                                product.getName()
                                    .toLowerCase()
                                    .contains(
                                            name.toLowerCase()
                                    )
                        )
                        .toList();

        response.put(
                "products",
                products
        );

        return response;
    }
}