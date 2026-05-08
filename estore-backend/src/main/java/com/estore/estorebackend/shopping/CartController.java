package com.estore.estorebackend.shopping;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestBody Map<String, Object> body) {
        return cartService.addToCart(
                Long.parseLong(body.get("userId").toString()),
                Long.parseLong(body.get("productId").toString()),
                Integer.parseInt(body.get("quantity").toString()));
    }

    @PutMapping("/update")
    public Cart updateItem(@RequestBody Map<String, Object> body) {
        return cartService.updateCartItem(
                Long.parseLong(body.get("userId").toString()),
                Long.parseLong(body.get("itemId").toString()),
                Integer.parseInt(body.get("quantity").toString()));
    }

    @DeleteMapping("/remove/{userId}/{itemId}")
    public Cart removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        return cartService.removeFromCart(userId, itemId);
    }
}
