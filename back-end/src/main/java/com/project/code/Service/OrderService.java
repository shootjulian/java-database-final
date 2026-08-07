package com.project.code.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Store;

import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.StoreRepository;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {

        // 1. Find customer by email
        Customer customer =
                customerRepository.findByEmail(
                        placeOrderRequest.getCustomerEmail()
                );


        // 2. If the customer does not exist, create it
        if (customer == null) {

            customer = new Customer();

            customer.setName(
                    placeOrderRequest.getCustomerName()
            );

            customer.setEmail(
                    placeOrderRequest.getCustomerEmail()
            );

            customer.setPhone(
                    placeOrderRequest.getCustomerPhone()
            );

            customer = customerRepository.save(customer);
        }


        // 3. Find the store
        Store store =
                storeRepository
                        .findById(placeOrderRequest.getStoreId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Store not found"
                                )
                        );


        // 4. Create the general order
        OrderDetails orderDetails =
                new OrderDetails();

        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);

        orderDetails.setTotalPrice(
                placeOrderRequest.getTotalPrice()
        );

        orderDetails.setDate(
                LocalDateTime.now()
        );

        orderDetails =
                orderDetailsRepository.save(orderDetails);


        // 5. Process every purchased product
        for (PurchaseProductDTO purchasedProduct
                : placeOrderRequest.getPurchaseProduct()) {


            // Find the actual Product entity
            Product product =
                    productRepository
                            .findById(purchasedProduct.getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found"
                                    )
                            );


            // Find the inventory for this product and store
            Inventory inventory =
                    inventoryRepository
                            .findByProductIdAndStoreId(
                                    product.getId(),
                                    store.getId()
                            );


            // Product is not available in this store
            if (inventory == null) {
                throw new RuntimeException(
                        "Inventory not found"
                );
            }


            // Check that there is enough stock
            if (inventory.getStockLevel()
                    < purchasedProduct.getQuantity()) {

                throw new RuntimeException(
                        "Not enough stock for product: "
                                + product.getName()
                );
            }


            // 6. Reduce stock
            inventory.setStockLevel(
                    inventory.getStockLevel()
                            - purchasedProduct.getQuantity()
            );

            inventoryRepository.save(inventory);


            // 7. Create OrderItem
            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(orderDetails);
            orderItem.setProduct(product);

            orderItem.setQuantity(
                    purchasedProduct.getQuantity()
            );

            orderItem.setPrice(
                    purchasedProduct.getPrice()
            );


            // 8. Save OrderItem
            orderItemRepository.save(orderItem);
        }
    }
}