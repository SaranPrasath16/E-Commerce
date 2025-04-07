package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Categories;

@Repository
public interface CategoryRepo extends MongoRepository<Categories, String>{

    @Query("{ 'categoryName': { $regex: ?0, $options: 'i' } }")
    Categories findCategoryIdByName(String categoryName);
}
