package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dto.ProductDeleteResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductUpdateRequestDTO;
import com.ecommerce.middleware.AuthRequired;
import com.ecommerce.model.Review;
import com.ecommerce.services.admin.ProductAdminImpl;
import com.ecommerce.services.user.UserImpl;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/productadmin")
public class ProductController {
	private final ProductAdminImpl productAdminImpl;
	private final UserImpl userImpl;
 
    public ProductController(ProductAdminImpl productAdminImpl, UserImpl userImpl) {
		super();
		this.productAdminImpl = productAdminImpl;
		this.userImpl = userImpl;
	}

	@GetMapping("/product")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getAllProduct(HttpServletRequest request) {
        List<ProductGetResponseDTO> productDTOList = productAdminImpl.getAllProduct();
        return ResponseEntity.ok(productDTOList);
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> addProduct(
            @RequestParam("category") String category,
            @RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productPrice") double productPrice,
            @RequestParam("noOfStocks") int noOfStocks,
            @RequestParam("images") MultipartFile[] images,
            HttpServletRequest request) {

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setCategory(category);
        productRequestDTO.setProductName(productName);
        productRequestDTO.setProductDescription(productDescription);
        productRequestDTO.setProductPrice(productPrice);
        productRequestDTO.setNoOfStocks(noOfStocks);

        String msg = productAdminImpl.addProduct(productRequestDTO, images);
        return ResponseEntity.ok(msg);
    }
    
    @PutMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> updateByName(
            @RequestParam("productName") String productName,
            @RequestParam("productNewName") String productNewName,
            @RequestParam("productNewCategory") String productNewCategory,
            @RequestParam("productNewDescription") String productNewDescription,
            @RequestParam("productNewPrice") double productNewPrice,
            @RequestParam("productNewStock") int productNewStock,
            @RequestParam("imagesToDelete") List<String> imagesToDelete,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            HttpServletRequest request) {

        ProductUpdateRequestDTO productUpdateRequestDTO = new ProductUpdateRequestDTO();
        productUpdateRequestDTO.setProductName(productName);
        productUpdateRequestDTO.setProductNewName(productNewName);
        productUpdateRequestDTO.setProductNewCategory(productNewCategory);
        productUpdateRequestDTO.setProductNewDescription(productNewDescription);
        productUpdateRequestDTO.setProductNewPrice(productNewPrice);
        productUpdateRequestDTO.setProductNewStock(productNewStock);
        productUpdateRequestDTO.setImagesToDelete(imagesToDelete);

        String msg = productAdminImpl.updateProductByName(productUpdateRequestDTO, images);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/product")
    @AuthRequired
    public ResponseEntity<String> deleteByName(@RequestParam("productName") String productName){
        ProductDeleteResponseDTO productDeleteResponseDTO = productAdminImpl.deleteProductByName(productName);
        return ResponseEntity.ok(productDeleteResponseDTO.getProductMsg()+" and the Product Id: "+productDeleteResponseDTO.getId());
    }
    
    @GetMapping("/product/review")
    public ResponseEntity<List<Review>> getProductReviews(@RequestParam("productId") String productId){
        List<Review> reviewList = userImpl.getProductReviews(productId);
        return ResponseEntity.ok(reviewList);
    }

}
