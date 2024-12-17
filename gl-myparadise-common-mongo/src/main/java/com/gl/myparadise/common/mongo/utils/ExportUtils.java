package com.gl.myparadise.common.mongo.utils;

import com.gl.myparadise.common.mongo.file.MongoExport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class ExportUtils {

    public static <T> byte[] getExportData(List<T> exportData, boolean isNewDoc) {
        if (exportData == null || exportData.isEmpty()) {
            return null;
        }
        Class<?> classz;
        T exportElement;
        Field[] fields;
        String title;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (isNewDoc) {
                // BOM标识
                outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
                // 表头
                exportElement = exportData.get(0);
                title = getTitle(exportElement.getClass());
                outputStream.write(title.getBytes(StandardCharsets.UTF_8));
                outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            for (int i = 0, len = exportData.size(); i < len; i++) {
                StringBuilder contentBuffer = new StringBuilder();
                exportElement = exportData.get(i);
                classz = exportElement.getClass();
                fields = classz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    MongoExport MongoExport = field.getAnnotation(MongoExport.class);
                    if (ObjectUtils.isEmpty(MongoExport)) {
                        continue;
                    }
                    Object obj = field.get(exportElement);
                    if (obj != null) {
                        contentBuffer.append("\"").append(obj).append("\"").append(",");
                    } else {
                        contentBuffer.append(",");
                    }
                }
                outputStream.write(contentBuffer.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            log.error("getExportData error", ex);
            return null;
        }
    }

    /**
     * 获取要导出数据的表头
     */
    public static <T> String getTitle(Class<T> classz) {
        StringBuilder titleBuffer = new StringBuilder();
        try {
            Field[] fields = classz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                MongoExport MongoExport = field.getAnnotation(MongoExport.class);
                if (ObjectUtils.isEmpty(MongoExport)) {
                    continue;
                }
                titleBuffer.append(MongoExport.name()).append(",");
            }
        } catch (Exception ex) {
            log.error("get export header title error", ex);
        }
        return titleBuffer.toString();
    }
}