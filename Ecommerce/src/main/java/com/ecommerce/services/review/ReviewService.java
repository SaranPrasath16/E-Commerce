package com.ecommerce.services.review;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
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
import com.mongodb.client.result.UpdateResult;

@Service
public class ReviewService {
	
	private final ReviewRepo reviewRepo;
	private final UserRepo userRepo;
	private final ProductRepo productRepo;
	private final MongoTemplate mongoTemplate;

	public ReviewService(ReviewRepo reviewRepo, UserRepo userRepo, ProductRepo productRepo,
			MongoTemplate mongoTemplate) {
		super();
		this.reviewRepo = reviewRepo;
		this.userRepo = userRepo;
		this.productRepo = productRepo;
		this.mongoTemplate = mongoTemplate;
	}

	public List<Review> getProductReviews(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new InvalidInputException("Product ID is empty.");
        }
        List<Review> reviewList=reviewRepo.findByProductId(productId);
        if(!reviewList.isEmpty()){
            return reviewList;
        }
        throw new ResourceNotFoundException("Reviews not found for the product.");
	}

	public String addProductReview(ReviewRequestDTO reviewRequestDTO) {
        String userId = JwtAspect.getCurrentUserId();
        if (userId.isEmpty() || userId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }
        
        User uId=userRepo.findUserById(userId);
        Product productId=productRepo.findProductById(reviewRequestDTO.getProductId());
 
        Review review = new Review(uId, productId,
                reviewRequestDTO.getRating(), reviewRequestDTO.getComment());
        Optional<Review> optionalReview = Optional.ofNullable(reviewRepo.save(review));
        if(optionalReview!=null) {
        	return "Review Successfully added for the Product";
        }
        throw new EntityPushException("Failed to add review to product.");
	}

	public String updateProductReview(ReviewUpdateRequestDTO reviewUpdateRequestDTO) {
	    String id = JwtAspect.getCurrentUserId();
	    if (id == null || id.isEmpty()) {
	        throw new ResourceNotFoundException("User ID not found in JWT token.");
	    }

	    String reviewId = reviewUpdateRequestDTO.getReviewId();
	    double rating = reviewUpdateRequestDTO.getRating();
	    String comments = reviewUpdateRequestDTO.getComment();

	    Review review = reviewRepo.findByReviewId(reviewId);
	    System.out.println(review);
	    String userId=review.getUserId().getUserId();
	    System.out.println(userId);
	    if(id==userId) {
		    if (review != null) {
		        if (review.getRating() == rating && review.getComment().equals(comments)) {
		            return "No changes detected in the review.";
		        }
		        Query query = new Query(Criteria.where("_id").is(reviewId));
		        Update update = new Update()
		                .set("rating", rating)
		                .set("comment", comments);

		        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Review.class);
		        if (updateResult.getModifiedCount() > 0) {
		            return "Updated Review Successfully";
		        }
		        throw new EntityUpdationException("Failed to update review.");
		    }	
	    }
	    throw new ResourceNotFoundException("Review with specified credentials not found.");
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
