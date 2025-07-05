package com.osho.journalApp.repository;

import com.osho.journalApp.entities.ConfigAppEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends MongoRepository<ConfigAppEntity, ObjectId> {
}
