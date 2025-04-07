   package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.ecommerce.model.User;

@Repository
public interface UserRepo extends MongoRepository<User, String>{
    
	@Query("{ 'email' : ?0 }")
    User findByEmail(String email);

}

