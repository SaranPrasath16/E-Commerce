package com.ecommerce.services.admin;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dto.ProductDeleteResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductUpdateRequestDTO;
import com.ecommerce.model.Product;
import com.ecommerce.services.product.ProductService;

@Service
public class ProductAdminImpl {
	private final ProductService productService;

	public ProductAdminImpl(ProductService productService) {
		super();
		this.productService = productService;
	}

	public String addProduct(ProductRequestDTO productRequestDTO, MultipartFile[] images) {
	       return productService.addProduct(productRequestDTO, images);
	}

	public String updateProductById(ProductUpdateRequestDTO productUpdateRequestDTO, MultipartFile[] images) {
		return productService.updateProductById(productUpdateRequestDTO, images);
	}

	public List<ProductGetResponseDTO> getAllProduct() {
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

	public ProductDeleteResponseDTO deleteProductByName(String productId) {
	    return productService.deleteById(productId);
	}
	
}
