package com.estore.estorebackend.shopping;

import com.estore.estorebackend.catalog.Product;
import com.estore.estorebackend.catalog.ProductRepository;
import com.estore.estorebackend.customer.User;
import com.estore.estorebackend.customer.UserRepository;
import com.estore.estorebackend.exception.ResourceNotFoundException;
import com.estore.estorebackend.inventory.Inventory;
import com.estore.estorebackend.inventory.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository,
                       InventoryRepository inventoryRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    return cartRepository.save(Cart.builder().user(user).build());
                });
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        existing -> {
                            int newTotal = existing.getQuantity() + quantity;
                            if (inventory.getQuantity() < newTotal)
                                throw new RuntimeException("Insufficient stock. Available: "
                                        + inventory.getQuantity() + ", in cart: " + existing.getQuantity());
                            existing.setQuantity(newTotal);
                        },
                        () -> {
                            if (inventory.getQuantity() < quantity)
                                throw new RuntimeException("Insufficient stock. Available: " + inventory.getQuantity());
                            cart.getItems().add(CartItem.builder()
                                    .cart(cart).product(product).quantity(quantity).build());
                        }
                );

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItem(Long userId, Long itemId, int quantity) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity <= 0) {
                        cart.getItems().remove(item);
                    } else {
                        Inventory inventory = inventoryRepository
                                .findByProductId(item.getProduct().getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
                        if (inventory.getQuantity() < quantity)
                            throw new RuntimeException("Insufficient stock. Available: " + inventory.getQuantity());
                        item.setQuantity(quantity);
                    }
                });
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeFromCart(Long userId, Long itemId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
