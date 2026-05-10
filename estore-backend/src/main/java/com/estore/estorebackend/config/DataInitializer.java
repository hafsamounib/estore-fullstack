package com.estore.estorebackend.config;

import com.estore.estorebackend.catalog.Category;
import com.estore.estorebackend.catalog.CategoryRepository;
import com.estore.estorebackend.catalog.Product;
import com.estore.estorebackend.catalog.ProductRepository;
import com.estore.estorebackend.customer.User;
import com.estore.estorebackend.customer.UserRepository;
import com.estore.estorebackend.entity.Profile;
import com.estore.estorebackend.inventory.Inventory;
import com.estore.estorebackend.inventory.InventoryRepository;
import com.estore.estorebackend.repository.ProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final InventoryRepository inventoryRepo;
    private final UserRepository userRepo;
    private final ProfileRepository profileRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryRepository categoryRepo,
                           ProductRepository productRepo,
                           InventoryRepository inventoryRepo,
                           UserRepository userRepo,
                           ProfileRepository profileRepo,
                           PasswordEncoder passwordEncoder) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.inventoryRepo = inventoryRepo;
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (categoryRepo.count() > 0) return;

        // --- Categories ---
        Category electronics = categoryRepo.save(Category.builder()
                .name("Electronics").description("Phones, laptops and accessories").build());
        Category books = categoryRepo.save(Category.builder()
                .name("Books").description("Programming and science books").build());
        Category sport = categoryRepo.save(Category.builder()
                .name("Sport").description("Equipment and clothing").build());

        // --- Products (2 per category = 6 total) ---
        seed("Samsung Galaxy S24",   "Android flagship smartphone",         7499.0,
             "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400", electronics, 15);
        seed("MacBook Air M2",       "13-inch, 8GB RAM, 256GB SSD",         12999.0,
             "https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=400", electronics, 10);

        seed("Clean Code",           "Robert C. Martin - best practices",   299.0,
             "https://images.unsplash.com/photo-1589998059171-988d887df646?w=400", books, 50);
        seed("Design Patterns",      "Gang of Four - software patterns",    379.0,
             "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400", books, 35);

        seed("Football",             "Official size 5, FIFA approved",      199.0,
             "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?w=400", sport, 60);
        seed("Running Shoes Nike",   "Lightweight training shoes",          699.0,
             "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", sport, 35);

        // --- Test users ---
        if (!userRepo.existsByEmail("admin@estore.com"))
            userRepo.save(User.builder()
                    .name("Admin")
                    .email("admin@estore.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .build());

        if (!userRepo.existsByEmail("user@estore.com"))
            userRepo.save(User.builder()
                    .name("Test User")
                    .email("user@estore.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(User.Role.USER)
                    .build());

        // Créer le profil pour Test User
        User testUser = userRepo.findByEmail("user@estore.com").orElse(null);
        if (testUser != null && profileRepo.findByUserId(testUser.getId()).isEmpty()) {
            Profile profile = Profile.builder()
                    .user(testUser)
                    .phone("+212723781160")
                    .address("Al azhar bernoussi")
                    .city("Casablanca")
                    .country("Maroc")
                    .build();
            profileRepo.save(profile);
            System.out.println("✅ Profil Test User créé");
        }

        // Créer le profil pour Admin
        User adminUser = userRepo.findByEmail("admin@estore.com").orElse(null);
        if (adminUser != null && profileRepo.findByUserId(adminUser.getId()).isEmpty()) {
            Profile profile = Profile.builder()
                    .user(adminUser)
                    .phone("+212600000000")
                    .address("FSBM")
                    .city("Casablanca")
                    .country("Maroc")
                    .build();
            profileRepo.save(profile);
            System.out.println("✅ Profil Admin créé");
        }

        System.out.println("✅ EStore data initialized successfully!");
    }

    private void seed(String name, String desc, Double price,
                      String imageUrl, Category category, int stock) {
        Product product = productRepo.save(Product.builder()
                .name(name)
                .description(desc)
                .price(price)
                .imageUrl(imageUrl)
                .category(category)
                .build());
        inventoryRepo.save(Inventory.builder()
                .product(product)
                .quantity(stock)
                .build());
    }
}
