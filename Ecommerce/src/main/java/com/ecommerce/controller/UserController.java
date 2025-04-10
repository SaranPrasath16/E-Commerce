package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.CartItemAddRequestDTO;
import com.ecommerce.dto.CartItemUpdateRequestDTO;
import com.ecommerce.dto.OrderAddResponseDTO;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.dto.ProductDescriptionListResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ReviewRequestDTO;
import com.ecommerce.dto.ReviewUpdateRequestDTO;
import com.ecommerce.middleware.AuthRequired;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Review;
import com.ecommerce.services.user.UserImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserImpl userImpl;

	public UserController(UserImpl userImpl) {
		this.userImpl = userImpl;
	}
	
    @GetMapping("/homepage")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getHomePage(){
        List<ProductGetResponseDTO> productList = userImpl.getHomePage();
        return  ResponseEntity.ok(productList);
    }
    
    @GetMapping("/cart")
    @AuthRequired
    public ResponseEntity<Cart> getBuyerCart(HttpServletRequest request){
    	Cart cart = userImpl.getCart();
        return ResponseEntity.ok(cart);
    }
    
    @PostMapping("/cart")
    @AuthRequired
    public ResponseEntity<String> addProductToCart(@RequestBody CartItemAddRequestDTO cartItemAddRequestDTO){
        String msg = userImpl.addProductToCart(cartItemAddRequestDTO);
        return ResponseEntity.ok(msg);
    }
    
    @PutMapping("/cart")
    @AuthRequired
    public ResponseEntity<String> updateCartItem(@RequestBody CartItemUpdateRequestDTO cartItemUpdateRequestDTO){
        String msg = userImpl.updateCartItem(cartItemUpdateRequestDTO);
        return ResponseEntity.ok(msg);
    }
    @DeleteMapping("/cart/{productId}")
    @AuthRequired
    public ResponseEntity<String> deleteCartItem(@PathVariable("productId") String productId){
        String msg = userImpl.deleteCartItem(productId);
        return ResponseEntity.ok(msg);
    }
    
    @PostMapping("/order")
    @AuthRequired
    public ResponseEntity<OrderAddResponseDTO> placeOrder(){
        OrderAddResponseDTO orderResponse = userImpl.placeOrder();

        HttpStatus status;
        if (!orderResponse.getSuccessfulOrders().isEmpty() && orderResponse.getOutOfStockItems().isEmpty() && orderResponse.getFailedOrders().isEmpty()) {
            status = HttpStatus.OK;
        } else if (!orderResponse.getSuccessfulOrders().isEmpty()) {
            status = HttpStatus.PARTIAL_CONTENT;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status).body(orderResponse);
    }
    
    @GetMapping("/orders")
    @AuthRequired
    public ResponseEntity<OrderGetResponseDTO> getAllOrders(){
        OrderGetResponseDTO orderGetResponseDTO = userImpl.getAllOrders();
        return ResponseEntity.ok(orderGetResponseDTO);
    }
    
    @GetMapping("/order/{orderId}")
    @AuthRequired
    public ResponseEntity<Orders> getOrder(@PathVariable("orderId") String orderId){
    	Orders order = userImpl.getSpecificOrder(orderId);
        return ResponseEntity.ok(order);
    }
    
    @DeleteMapping("/order/{orderId}")
    @AuthRequired
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") String orderId){
        String msg = userImpl.deleteOrder(orderId);
        return ResponseEntity.ok(msg);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<ProductDescriptionListResponseDTO> getProductByPriceRange(@RequestParam("minPrice") double minPrice, @RequestParam("maxPrice") double maxPrice){
        ProductDescriptionListResponseDTO productDescriptionListResponseDTO = userImpl.getProductByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(productDescriptionListResponseDTO);
    }
    
    @GetMapping("/product/review")
    public ResponseEntity<List<Review>> getProductReviews(@RequestParam("productId") String productId){
        List<Review> reviewList = userImpl.getProductReviews(productId);
        return ResponseEntity.ok(reviewList);
    }
    
    @PostMapping("/product/review")
    @AuthRequired
    public ResponseEntity<String> addReview(@RequestBody ReviewRequestDTO reviewRequestDTO){
        String msg = userImpl.addProductReviews(reviewRequestDTO);
        return ResponseEntity.ok(msg);
    }
    
    @PutMapping("/product/review")
    @AuthRequired
    public ResponseEntity<String> updateReview(@RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO){
        String msg = userImpl.updateProductReviews(reviewUpdateRequestDTO);
        return ResponseEntity.ok(msg);
    }
    
    @DeleteMapping("/product/review")
    @AuthRequired
    public ResponseEntity<String> deleteReview(@RequestParam("reviewId") String reviewId){
        String msg = userImpl.deleteProductReviews(reviewId);
        return ResponseEntity.ok(msg);
    }
}
