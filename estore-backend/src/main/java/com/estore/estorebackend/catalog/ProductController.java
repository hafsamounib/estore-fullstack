package com.estore.estorebackend.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/categories")
    public List<Category> getCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("/api/products")
    public List<Product> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        if (search != null && !search.isBlank())
            return productService.searchProducts(search);
        if (categoryId != null)
            return productService.getProductsByCategory(categoryId);
        return productService.getAllProducts();
    }

    @GetMapping("/api/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/api/products")
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> body) {
        Product p = new Product();
        p.setName((String) body.get("name"));
        p.setDescription((String) body.get("description"));
        p.setPrice(Double.parseDouble(body.get("price").toString()));
        p.setImageUrl((String) body.get("imageUrl"));
        if (body.get("categoryId") != null) {
            Category cat = new Category();
            cat.setId(Long.parseLong(body.get("categoryId").toString()));
            p.setCategory(cat);
        }
        Integer stock = body.get("stock") != null
                ? Integer.parseInt(body.get("stock").toString()) : 0;
        return ResponseEntity.status(201).body(productService.createProduct(p, stock));
    }
}
