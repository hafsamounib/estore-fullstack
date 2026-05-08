package com.estore.estorebackend.catalog;

import com.estore.estorebackend.exception.ResourceNotFoundException;
import com.estore.estorebackend.inventory.Inventory;
import com.estore.estorebackend.inventory.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Product createProduct(Product product, Integer stock) {
        Product saved = productRepository.save(product);
        inventoryRepository.save(Inventory.builder()
                .product(saved).quantity(stock != null ? stock : 0).build());
        return saved;
    }
}
