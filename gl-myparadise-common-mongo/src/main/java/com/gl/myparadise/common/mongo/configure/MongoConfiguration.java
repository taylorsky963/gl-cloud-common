package com.gl.myparadise.common.mongo.configure;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Configuration
public class MongoConfiguration {

    @Bean
    public GridFSBucket getGridFSBucket(MongoTemplate mongoTemplate) {
        MongoDatabase db = mongoTemplate.getDb();
        ensureCollectionExists(db, "fs.files");
        ensureCollectionExists(db, "fs.chunks");
        ensureCollectionExists(db, "gl_export");
        return GridFSBuckets.create(db);
    }

    private void ensureCollectionExists(MongoDatabase db, String collectionName) {
        try {
            db.createCollection(collectionName);
            log.info("Created collection: {}", collectionName);
        } catch (MongoCommandException e) {
            if (e.getErrorCode() == 48) {
                log.debug("Collection already exists: {}", collectionName);
            } else {
                log.warn("Failed to create collection: {}", collectionName, e);
            }
        }
    }
}