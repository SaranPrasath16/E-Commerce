package com.ecommerce.services.review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dto.ReviewGetResponseDTO;
import com.ecommerce.dto.ReviewRequestDTO;
import com.ecommerce.dto.ReviewUpdateRequestDTO;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.EntityPushException;
import com.ecommerce.exceptionhandler.EntityUpdationException;
import com.ecommerce.exceptionhandler.InvalidInputException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.middleware.JwtAspect;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.repo.ProductRepo;
import com.ecommerce.repo.ReviewRepo;
import com.ecommerce.repo.UserRepo;
import com.ecommerce.services.cloudinary.CloudinaryService;
import com.mongodb.client.result.UpdateResult;

@Service
public class ReviewService {
	
	private final ReviewRepo reviewRepo;
	private final UserRepo userRepo;
	private final ProductRepo productRepo;
	private final MongoTemplate mongoTemplate;
	private final CloudinaryService cloudinaryService;

	public ReviewService(ReviewRepo reviewRepo, UserRepo userRepo, ProductRepo productRepo, MongoTemplate mongoTemplate,
			CloudinaryService cloudinaryService) {
		super();
		this.reviewRepo = reviewRepo;
		this.userRepo = userRepo;
		this.productRepo = productRepo;
		this.mongoTemplate = mongoTemplate;
		this.cloudinaryService = cloudinaryService;
	}

	public List<ReviewGetResponseDTO> getProductReviews(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new InvalidInputException("Product ID is empty.");
        }
        List<Review> reviewList=reviewRepo.findByProductId(productId);
        System.out.println(reviewList);
        if(!reviewList.isEmpty()){
            return reviewList.stream()
                    .map(review -> new ReviewGetResponseDTO(
                            review.getUserId().getUserName(),
                            review.getProductId().getProductName(),
                            review.getRating(),
                            review.getComment(),
                            review.getUserImageUrls()
                    ))
                    .collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Reviews not found for the product.");
	}

	public String addProductReviews(ReviewRequestDTO reviewRequestDTO, MultipartFile[] userImageUrls) {
	    if (reviewRequestDTO.getProductId() == null || reviewRequestDTO.getProductId().isEmpty()) {
	        throw new InvalidInputException("Product ID is required.");
	    }

	    Product product = productRepo.findById(reviewRequestDTO.getProductId())
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

	    List<String> imageUrls = new ArrayList<>();
	    if (userImageUrls != null && userImageUrls.length > 0) {
	        for (MultipartFile image : userImageUrls) {
	            if (!image.isEmpty()) {
	                try {
	                    String imageUrl = cloudinaryService.uploadImage(image);
	                    imageUrls.add(imageUrl);
	                } catch (IOException e) {
	                    throw new EntityPushException("Failed to upload image to Cloudinary");
	                }
	            }
	        }
	    }

	    User user = userRepo.findById(JwtAspect.getCurrentUserId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Review review = new Review();
	    review.setProductId(product);
	    review.setUserId(user);
	    review.setRating(reviewRequestDTO.getRating());
	    review.setComment(reviewRequestDTO.getComment());
	    review.setUserImageUrls(imageUrls);

	    reviewRepo.save(review);

	    return "Review added successfully";
	}
	
	public String updateProductReview(ReviewUpdateRequestDTO reviewUpdateRequestDTO, MultipartFile[] userImageUrls) {
	    String userIdFromToken = JwtAspect.getCurrentUserId();
	    if (userIdFromToken == null || userIdFromToken.isEmpty()) {
	        throw new ResourceNotFoundException("User ID not found in JWT token.");
	    }

	    String reviewId = reviewUpdateRequestDTO.getReviewId();
	    double rating = reviewUpdateRequestDTO.getRating();
	    String comment = reviewUpdateRequestDTO.getComment();
	    List<String> userImageToDelete = reviewUpdateRequestDTO.getUserImageToDelete();

	    Review review = reviewRepo.findByReviewId(reviewId);
	    if (review == null) {
	        throw new ResourceNotFoundException("Review not found.");
	    }
	    if (!review.getUserId().getUserId().equals(userIdFromToken)) {
	        throw new RuntimeException("You are not authorized to update this review.");
	    }
	    if (userImageToDelete != null && !userImageToDelete.isEmpty()) {
	        deleteImagesFromReview(review, userImageToDelete);
	    }
	    Review updatedReview = reviewRepo.findByReviewId(reviewId);
	    List<String> finalImageUrls = new ArrayList<>(updatedReview.getUserImageUrls());

	    if (userImageUrls != null && userImageUrls.length > 0) {
	        for (MultipartFile image : userImageUrls) {
	            if (image != null && !image.isEmpty()) {
	                try {
	                    String imageUrl = cloudinaryService.uploadImage(image);
	                    finalImageUrls.add(imageUrl);
	                } catch (IOException e) {
	                    throw new EntityPushException("Failed to upload image to Cloudinary.");
	                }
	            }
	        }
	    }
	    Query query = new Query(Criteria.where("_id").is(reviewId));
	    Update update = new Update()
	            .set("rating", rating)
	            .set("comment", comment)
	            .set("userImageUrls", finalImageUrls);

	    UpdateResult result = mongoTemplate.updateFirst(query, update, Review.class);

	    if (result.getModifiedCount() > 0) {
	        return "Updated Review Successfully";
	    }

	    throw new EntityUpdationException("Failed to update review.");
	}

	
	private void deleteImagesFromReview(Review review, List<String> imagesToDelete) {
	    List<String> updatedImageUrls = new ArrayList<>(review.getUserImageUrls());
	    updatedImageUrls.removeAll(imagesToDelete);
	    System.out.println(updatedImageUrls);
	    Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(review.getReviewId()));

	    Update update = new Update();
	    update.set("userImageUrls", updatedImageUrls);
	    mongoTemplate.updateFirst(query, update, Review.class);

	    for (String imageUrl : imagesToDelete) {
	        String publicId = cloudinaryService.extractPublicIdFromUrl(imageUrl);
	        cloudinaryService.deleteImageFromCloudinary(publicId);
	    }
	}

	public String deleteProductReview(String reviewId) {
        String id = JwtAspect.getCurrentUserId();
        if (id.isEmpty() || id ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }
        long i = reviewRepo.deleteByIdAndUserId(reviewId,id);
        if(i > 0){
            return "Review Deleted Successfully";
        }
        throw new EntityDeletionException("Failed to delete the review.");
	}

}
