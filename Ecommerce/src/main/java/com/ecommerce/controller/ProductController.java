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
import com.ecommerce.services.admin.ProductAdminImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/productadmin")
public class ProductController {
	private final ProductAdminImpl productAdminImpl;
	
    public ProductController(ProductAdminImpl productAdminImpl) {
		super();
		this.productAdminImpl = productAdminImpl;
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
            @RequestParam("jsonData") String jsonData,
            @RequestParam("images") MultipartFile[] images,
            HttpServletRequest request) {
        
        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequestDTO productRequestDTO;
        
        try {
            productRequestDTO = objectMapper.readValue(jsonData, ProductRequestDTO.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON Data");
        }

        String msg = productAdminImpl.addProduct(productRequestDTO, images);
        return ResponseEntity.ok(msg);
    }
	
    @PutMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> updateByName(
            @RequestParam("jsonData") String jsonData,
            @RequestParam("images") MultipartFile[] images,
            HttpServletRequest request) {
        
        ObjectMapper objectMapper = new ObjectMapper();
        ProductUpdateRequestDTO productUpdateRequestDTO;

        try {
            productUpdateRequestDTO = objectMapper.readValue(jsonData, ProductUpdateRequestDTO.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON Data");
        }

        String msg = productAdminImpl.updateProductByName(productUpdateRequestDTO, images);
        return ResponseEntity.ok(msg);
    }
    
    @DeleteMapping("/product")
    @AuthRequired
    public ResponseEntity<String> deleteByName(@RequestParam("productName") String productName){
        ProductDeleteResponseDTO productDeleteResponseDTO = productAdminImpl.deleteProductByName(productName);
        return ResponseEntity.ok(productDeleteResponseDTO.getProductMsg()+" and the Product Id: "+productDeleteResponseDTO.getId());
    }

}
