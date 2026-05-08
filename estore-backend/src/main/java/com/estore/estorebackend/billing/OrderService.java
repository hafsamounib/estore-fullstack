package com.estore.estorebackend.billing;

import com.estore.estorebackend.customer.User;
import com.estore.estorebackend.customer.UserRepository;
import com.estore.estorebackend.exception.ResourceNotFoundException;
import com.estore.estorebackend.inventory.Inventory;
import com.estore.estorebackend.inventory.InventoryRepository;
import com.estore.estorebackend.shopping.Cart;
import com.estore.estorebackend.shopping.CartItem;
import com.estore.estorebackend.shopping.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartService cartService,
                        InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Order placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartService.getCartByUserId(userId);

        if (cart.getItems().isEmpty())
            throw new RuntimeException("Cart is empty");

        for (CartItem item : cart.getItems()) {
            Inventory inv = inventoryRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
            if (inv.getQuantity() < item.getQuantity())
                throw new RuntimeException("Insufficient stock for: " + item.getProduct().getName());
        }

        Order order = Order.builder()
                .user(user)
                .totalAmount(cart.getTotal())
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(ci -> OrderItem.builder()
                        .order(order)
                        .product(ci.getProduct())
                        .quantity(ci.getQuantity())
                        .unitPrice(ci.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        cart.getItems().forEach(ci -> {
            Inventory inv = inventoryRepository.findByProductId(ci.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for: " + ci.getProduct().getName()));
            inv.setQuantity(inv.getQuantity() - ci.getQuantity());
            inventoryRepository.save(inv);
        });

        Order saved = orderRepository.save(order);
        cartService.clearCart(userId);
        return saved;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }
}
