package com.ecommerce.services.admin;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.model.User;
import com.ecommerce.repo.UserRepo;
import com.mongodb.client.result.DeleteResult;

@Service
public class AdminImpl {
	
	private final UserRepo userRepo;
	private final MongoTemplate mongoTemplate;


	public AdminImpl(UserRepo userRepo, MongoTemplate mongoTemplate) {
		super();
		this.userRepo = userRepo;
		this.mongoTemplate = mongoTemplate;
	}

	public List<User> getAllUsers() {
        List<User> buyerList = userRepo.findAll();
        if(!buyerList.isEmpty()){
            return buyerList;
        }
        throw new ResourceNotFoundException("No Buyers found.");
    }

    public String deleteUser(String userId) {
        Query query = new Query(Criteria.where("_id").is(userId));
        DeleteResult deleteResult = mongoTemplate.remove(query, User.class);
        if(deleteResult.getDeletedCount()>0){
            return "Successfully deleted the User";
        }
        throw new EntityDeletionException("Failed to delete user with given credentials");
    }
	

}
