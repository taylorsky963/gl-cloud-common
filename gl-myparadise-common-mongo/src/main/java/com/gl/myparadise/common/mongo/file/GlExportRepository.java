package com.gl.myparadise.common.mongo.file;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GlExportRepository extends MongoRepository<GlExport, String> {
    List<GlExport> findByUserIdOrderByCreateDateDesc(Long userId);
    void deleteAllByUserId(Long userId);
}
