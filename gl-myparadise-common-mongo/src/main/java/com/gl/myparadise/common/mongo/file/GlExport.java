package com.gl.myparadise.common.mongo.file;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "gl_export") // 指定 MongoDB 集合名稱
public class GlExport {

    @Id
    private String id;

    private String isDelete = "0"; // 是否删除 (0: 未删除, 1: 已删除)
    private String createUserName;
    private Long createUserId;
    private Date createDate;
    private String updateUserName;
    private Long updateUserId;
    private Date updateTime;
    private String title;
    private Integer userId;
    private String queryParamData;
    private Date completeDate;
    private String filePath;
    private String fileId;
    private Integer status; // 文件下载状态 (0: 下载中, 1: 下载成功, 2: 下载失败)
    private String remainingTime;
}
