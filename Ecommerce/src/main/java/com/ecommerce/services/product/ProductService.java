package com.ecommerce.services.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dto.ProductDeleteResponseDTO;
import com.ecommerce.dto.ProductDescriptionListResponseDTO;
import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductUpdateRequestDTO;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.EntityPushException;
import com.ecommerce.exceptionhandler.EntityUpdationException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repo.ProductRepo;
import com.ecommerce.services.cloudinary.CloudinaryService;
import com.mongodb.client.result.UpdateResult;

@Service
public class ProductService {
	
	private final ProductRepo productRepo;
	private final CloudinaryService cloudinaryService;
	private final MongoTemplate mongoTemplate;

	
	public ProductService(ProductRepo productRepo, CloudinaryService cloudinaryService,
			MongoTemplate mongoTemplate) {
		super();
		this.productRepo = productRepo;
		this.cloudinaryService = cloudinaryService;
		this.mongoTemplate = mongoTemplate;
	}

	public List<Product> getAllProducts() {
        List<Product> productList = productRepo.findAll();
        if(!productList.isEmpty()){
            return productList;
        }
        throw new ResourceNotFoundException("No Products found.");
    }
	
    public int getAvailableStock(String productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if(optionalProduct.isPresent()){
            return optionalProduct.get().getNoOfStocks();
        }
        return 0;
    }
    
    public boolean updateProductStock(String productId, int quantity) {
        Optional<Product> productOptional = productRepo.findById(productId);

        if (productOptional.isPresent()) {
        	Product product = productOptional.get();
            int availableStock = product.getNoOfStocks();

            if (availableStock >= quantity) {
                product.setNoOfStocks(availableStock - quantity);
                productRepo.save(product);
                return true;
            }
        }
        return false;
    }

	public ProductDescriptionListResponseDTO getProductByPriceRange(double minPrice, double maxPrice) {
        List<Product> productList = productRepo.findByPriceBetween(minPrice,maxPrice);
        if(!productList.isEmpty()){
            return new ProductDescriptionListResponseDTO(productList);
        }
        throw new ResourceNotFoundException("Failed to fetch products with provided credentials.");
    }

	public String addProduct(ProductRequestDTO productRequestDTO, MultipartFile[] images) {
        
	    List<String> imageUrls = new ArrayList<>();
	    
	    if (images != null && images.length > 0) {
	        for (MultipartFile image : images) {
	            if (image != null && !image.isEmpty()) {
	                try {
	                    String imageUrl = cloudinaryService.uploadImage(image);
	                    imageUrls.add(imageUrl);
	                } catch (IOException e) {
	                    throw new EntityPushException("Failed to upload image to Cloudinary");
	                }
	            }
	        }
	    }
        Product product = new Product(
                productRequestDTO.getCategory(),
                productRequestDTO.getProductName(),
                productRequestDTO.getProductDescription(),
                productRequestDTO.getProductPrice(),
                productRequestDTO.getNoOfStocks(),
                imageUrls
        );
        Optional<Product> optionalProduct = Optional.ofNullable(productRepo.save(product));

        if(optionalProduct.isPresent()){
           return "Product added successfully";
        }
            throw new EntityPushException("Failed to add product to database");
	}

	public String updateProductById(ProductUpdateRequestDTO productUpdateRequestDTO, MultipartFile[] images) {
        List<String> imagesToDelete = productUpdateRequestDTO.getImagesToDelete();
        
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(productUpdateRequestDTO.getProductId()));
        Product product = mongoTemplate.findOne(query, Product.class);
        
        if (imagesToDelete != null && !imagesToDelete.isEmpty() && product != null) {
            deleteImagesFromProduct(product, imagesToDelete);
        }
        Update update = new Update();
        if (productUpdateRequestDTO.getProductNewCategory() != null) update.set("category", productUpdateRequestDTO.getProductNewCategory());
        if (productUpdateRequestDTO.getProductNewName() != null) update.set("productName", productUpdateRequestDTO.getProductNewName());
        if (productUpdateRequestDTO.getProductNewDescription() != null) update.set("productDescription", productUpdateRequestDTO.getProductNewDescription());
        if (productUpdateRequestDTO.getProductNewPrice() != 0) update.set("productPrice", productUpdateRequestDTO.getProductNewPrice());
        if (productUpdateRequestDTO.getProductNewStock() != 0) update.set("noOfStocks", productUpdateRequestDTO.getProductNewStock());
        
        if (images != null && images.length > 0) {
            List<String> newImageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    try {
                        String imageUrl = cloudinaryService.uploadImage(image);
                        newImageUrls.add(imageUrl);
                    } catch (IOException e) {
                        throw new EntityPushException("Failed to upload image to db");
                    }
                }
            }
            if (!newImageUrls.isEmpty()) {
                update.addToSet("imageUrls").each(newImageUrls.toArray());
            }
        }
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        System.out.println(result);
        if (result.getModifiedCount() > 0) {
            query = new Query();
            query.addCriteria(Criteria.where("_id").is(productUpdateRequestDTO.getProductId()));
            //Product updatedProduct = mongoTemplate.findOne(query, Product.class);
            return "Updated Product Successfully";
        }
        throw new EntityUpdationException("Failed to update the product in db");
	}
	
    private void deleteImagesFromProduct(Product product, List<String> imagesToDelete) {
        List<String> updatedImageUrls = new ArrayList<>(product.getImageUrls());
        updatedImageUrls.removeAll(imagesToDelete);
        System.out.println(updatedImageUrls);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(product.getProductId()));

        Update update = new Update();
        update.set("imageUrls", updatedImageUrls);
        mongoTemplate.updateFirst(query, update, Product.class);

	    for (String imageUrl : imagesToDelete) {
	        String publicId = cloudinaryService.extractPublicIdFromUrl(imageUrl);
	        cloudinaryService.deleteImageFromCloudinary(publicId);
	    }
    }
	public ProductDeleteResponseDTO deleteById(String productId) {
	    Optional<Product> product = productRepo.findById(productId);
	    if (product.isPresent()) {
	        productRepo.deleteById(productId);
	        return new ProductDeleteResponseDTO(productId, "Product Deleted Successfully");
	    }     
	    throw new EntityDeletionException("Failed to delete product: Product not found in database");
	}
}
