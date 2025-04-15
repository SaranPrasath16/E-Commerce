package com.ecommerce.services.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.ecommerce.dto.OrderAddResponseDTO;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.InvalidInputException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.middleware.JwtAspect;
import com.ecommerce.model.CartItems;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Orders;
import com.ecommerce.repo.OrderRepo;
import com.ecommerce.services.cart.CartService;
import com.ecommerce.services.product.ProductService;

@Service
public class OrderService {
	
	private final CartService cartService;
	private final ProductService productService;
	private final OrderRepo orderRepo;
	
	public OrderService(CartService cartService, ProductService productService, OrderRepo orderRepo) {
		super();
		this.cartService = cartService;
		this.productService = productService;
		this.orderRepo = orderRepo;
	}

	public OrderAddResponseDTO placeOrder() {

        String cartId = JwtAspect.getCurrentUserId();
        if (cartId == null || cartId.isEmpty()) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }
        Cart cart=cartService.getCart();
        List<CartItems> cartItems = cartService.getCartItems(cartId);
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("No items found in cart");
        }

        List<String> successful = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        List<String> outOfStock = new ArrayList<>();
        LocalDateTime orderDateTime = LocalDateTime.now();

        for (CartItems item : cartItems) {
            if (item.getQuantity() > productService.getAvailableStock(item.getProductId())) {
                outOfStock.add(item.getName());
                continue;
            }
            //String productId = item.getProductId().getProductId();
            Orders order = new Orders(cartId, cartItems,cart.getTotalAmount(),cart.getCartItems().size(), orderDateTime, "pending");
            Optional<Orders> savedOrder = Optional.ofNullable(orderRepo.save(order));
            if (savedOrder.isPresent()) {
                productService.updateProductStock(item.getProductId(), item.getQuantity());
                successful.add(order.getOrderId());
            } else {
                failed.add(item.getName());
            }
        }

        OrderAddResponseDTO response = new OrderAddResponseDTO();
        response.setSuccessfulOrders(successful);
        response.setOutOfStockItems(outOfStock);
        response.setFailedOrders(failed);
        cartService.deleteSelectedCartItems(cartId, cartItems);
        return response;
    }

	public OrderGetResponseDTO getAllOrders() {
        String userId = JwtAspect.getCurrentUserId();
        if (userId.isEmpty() || userId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }

        OrderGetResponseDTO orderGetResponseDTO = new OrderGetResponseDTO(orderRepo.findByUserId(userId));
        return orderGetResponseDTO;
	}

	public Orders getSpecificOrder(String orderId) {
        Optional<Orders> optionalOrder = orderRepo.findById(orderId);
        if(optionalOrder.isPresent()){
            return optionalOrder.get();
        }
        throw new ResourceNotFoundException("Failed to fetch order with specified credentials.");
	}

	public String deleteOrder(String orderId) {
        Optional<Orders> order = orderRepo.findById(orderId);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("Failed to fetch order with provided credentials.");
        }

        if(order.get().getStatus().equals("pending")){
            long i = orderRepo.deleteByOrderId(orderId);
            if(i > 0){
                return "Order deleted successfully.";
            }
            throw new EntityDeletionException("Failed to delete the order.");
        }
        throw new InvalidInputException("Invalid order status provided.");
	}
}


