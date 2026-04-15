package com.gl.myparadise.common.mongo.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class GlExportService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GlExportRepository glExportRepository;

    public void updateById(GlExport glExport) {
        Query query = new Query(Criteria.where("id").is(glExport.getId()));
        Update update = new Update();

        if (glExport.getTitle() != null) update.set("title", glExport.getTitle());
        if (glExport.getUserId() != null) update.set("userId", glExport.getUserId());
        if (glExport.getQueryParamData() != null) update.set("queryParamData", glExport.getQueryParamData());
        if (glExport.getCompleteDate() != null) update.set("completeDate", glExport.getCompleteDate());
        if (glExport.getFilePath() != null) update.set("filePath", glExport.getFilePath());
        if (glExport.getFileId() != null) update.set("fileId", glExport.getFileId());
        if (glExport.getStatus() != null) update.set("status", glExport.getStatus());
        if (glExport.getRemainingTime() != null) update.set("remainingTime", glExport.getRemainingTime());
        if (glExport.getUpdateUserName() != null) update.set("updateUserName", glExport.getUpdateUserName());
        if (glExport.getUpdateUserId() != null) update.set("updateUserId", glExport.getUpdateUserId());

        update.set("updateTime", new Date());

        mongoTemplate.updateFirst(query, update, GlExport.class);
    }

    public void insert(GlExport glExport) {
        glExport.setCreateDate(new Date().getTime());
        glExportRepository.save(glExport);
    }

    public void completeExport(String id, String mongoFileId, String title) {
        if (ObjectUtils.isEmpty(id)) {
            return;
        }
        GlExport export = new GlExport();
        export.setId(id);
        export.setCompleteDate(new Date().getTime());
        export.setFileId(mongoFileId);
        export.setFilePath(export.getFileId());
        export.setStatus(1);
        export.setRemainingTime(null);

        if (StringUtils.isNotBlank(title)) {
            export.setTitle(title);
        }

        updateById(export);
    }

    public void failExport(String id) {
        if (ObjectUtils.isEmpty(id)) {
            return;
        }
        GlExport export = new GlExport();
        export.setId(id);
        export.setCompleteDate(new Date().getTime());
        export.setStatus(2);
        updateById(export);
    }

    public void updateTitle(String id, String title, String time) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(title)) {
            return;
        }
        GlExport export = new GlExport();
        export.setId(id);
        export.setTitle(title);
        export.setRemainingTime(time);

        updateById(export);
    }

    public String startExport(Long userId, String title) {
        GlExport export = new GlExport();
        export.setCreateDate(new Date().getTime());
        export.setTitle(title);
        export.setUserId(userId);
        export.setStatus(0);
        GlExport inserted = glExportRepository.save(export);
        return inserted.getId();
    }

    public String getFilePath(final String fileId) {
        return "/bg/system/export/mgmt/download/" + fileId;
    }

    public List<GlExport> queryByUserId(Long userId) {
        return glExportRepository.findByUserIdOrderByCreateDateDesc(userId);
    }

    public void deleteByUserId(Long userId) {
        glExportRepository.deleteAllByUserId(userId);
    }

    public void deleteById(String id) {
        glExportRepository.deleteById(id);
    }
}
