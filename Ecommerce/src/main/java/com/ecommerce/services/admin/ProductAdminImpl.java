package com.ecommerce.services.admin;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dto.ProductDeleteResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductUpdateRequestDTO;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.middleware.JwtAspect;
import com.ecommerce.model.Product;
import com.ecommerce.repo.ProductRepo;
import com.ecommerce.services.product.ProductService;

@Service
public class ProductAdminImpl {
	private final ProductService productService;
	private final ProductRepo productRepo;

	public ProductAdminImpl(ProductService productService, ProductRepo productRepo) {
		super();
		this.productService = productService;
		this.productRepo = productRepo;
	}

	public String addProduct(ProductRequestDTO productRequestDTO, MultipartFile[] images) {
	       return productService.addProduct(productRequestDTO, images);
	}

	public String updateProductByName(ProductUpdateRequestDTO productUpdateRequestDTO, MultipartFile[] images) {
		return productService.updateProductByName(productUpdateRequestDTO, images);
	}

	public List<ProductGetResponseDTO> getAllProduct() {
        String userId = JwtAspect.getCurrentUserId();
        if (userId.isEmpty() || userId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }

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

	public ProductDeleteResponseDTO deleteProductByName(String productName) {
	    Product product = productRepo.findByProductName(productName);
	    
	    if (product == null) {
	        throw new ResourceNotFoundException("Product not found with name: " + productName);
	    }
	    
	    String productId = product.getProductId();
	    return productService.deleteById(productId);
	}
	
}
