package com.ecommerce.services.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.dto.CartItemAddRequestDTO;
import com.ecommerce.dto.CartItemUpdateRequestDTO;
import com.ecommerce.dto.OrderAddResponseDTO;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.dto.ProductDescriptionListResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Product;
import com.ecommerce.services.cart.CartService;
import com.ecommerce.services.order.OrderService;
import com.ecommerce.services.product.ProductService;

@Service
public class UserImpl {
	
	private final ProductService productService;
	private final CartService cartService;
	private final OrderService orderService;
	
    public UserImpl(ProductService productService, CartService cartService, OrderService orderService) {
		super();
		this.productService = productService;
		this.cartService = cartService;
		this.orderService = orderService;
	}
	public List<ProductGetResponseDTO> getHomePage() {    
        List<Product> productList = productService.getAllProducts();
        List<ProductGetResponseDTO> productDTOList = productList.stream()
                .map(product -> new ProductGetResponseDTO(
                        product.getCategoryId().getCategoryName(),
                        product.getProductName(),
                        product.getProductDescription(),
                        product.getProductPrice(),
                        product.getNoOfStocks(),
                        product.getImageUrls()
                ))
                .collect(Collectors.toList());

        return productDTOList;
    }

	public Cart getCart() {
        return cartService.getCart();
	}

	public String addProductToCart(CartItemAddRequestDTO cartItemAddRequestDTO) {
		return cartService.addProduct(cartItemAddRequestDTO);
	}

    public String updateCartItem(CartItemUpdateRequestDTO cartItemUpdateRequestDTO) {
        return cartService.updateCartItem(cartItemUpdateRequestDTO);
    }

    public String deleteCartItem(String productId) {
        return cartService.deleteCartItem(productId);
    }

    public OrderAddResponseDTO placeOrder() {
        return orderService.placeOrder();
    }
	public OrderGetResponseDTO getAllOrders() {
        return orderService.getAllOrders();
	}
	public Orders getSpecificOrder(String orderId) {
        return orderService.getSpecificOrder(orderId);
    }
	public String deleteOrder(String orderId) {
        return orderService.deleteOrder(orderId);
	}
	public ProductDescriptionListResponseDTO getProductByPriceRange(double minPrice, double maxPrice) {
        return productService.getProductByPriceRange(minPrice, maxPrice);
	}
}
