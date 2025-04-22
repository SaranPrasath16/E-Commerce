package com.ecommerce.services.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dto.CartItemAddRequestDTO;
import com.ecommerce.dto.CartItemUpdateRequestDTO;
import com.ecommerce.dto.OrderAddResponseDTO;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.dto.ProductDescriptionListResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ReviewGetResponseDTO;
import com.ecommerce.dto.ReviewRequestDTO;
import com.ecommerce.dto.ReviewUpdateRequestDTO;
import com.ecommerce.middleware.JwtAspect;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Product;
import com.ecommerce.services.cart.CartService;
import com.ecommerce.services.order.OrderService;
import com.ecommerce.services.product.ProductService;
import com.ecommerce.services.review.ReviewService;

@Service
public class UserImpl {
	
	private final ProductService productService;
	private final CartService cartService;
	private final OrderService orderService;
	private final ReviewService reviewService;
	
	public UserImpl(ProductService productService, CartService cartService, OrderService orderService,
			ReviewService reviewService) {
		super();
		this.productService = productService;
		this.cartService = cartService;
		this.orderService = orderService;
		this.reviewService = reviewService;
	}

	public List<ProductGetResponseDTO> getHomePage() {    
        List<Product> productList = productService.getAllProducts();
        List<ProductGetResponseDTO> productDTOList = productList.stream()
                .map(product -> new ProductGetResponseDTO(
                        product.getCategory(),
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
        String cartId = JwtAspect.getCurrentUserId();
        return cartService.getCart(cartId);
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

    public Map<String, Object> checkout() {
        return orderService.checkout();
    }
	public OrderAddResponseDTO placeOrder(String payload, String razorpaySignature) {
		return orderService.placeOrder(payload,razorpaySignature);
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
	public List<ReviewGetResponseDTO> getProductReviews(String productId) {
        return reviewService.getProductReviews(productId);
    }

	public String addProductReviews(ReviewRequestDTO reviewRequestDTO,MultipartFile[] userImageUrls) {
        return reviewService.addProductReviews(reviewRequestDTO,userImageUrls);
	}

	public String updateProductReviews(ReviewUpdateRequestDTO reviewUpdateRequestDTO,MultipartFile[] userImageUrls) {
        return reviewService.updateProductReview(reviewUpdateRequestDTO,userImageUrls);
	}

	public String deleteProductReviews(String reviewId) {
        return reviewService.deleteProductReview(reviewId);
	}
}

