package com.gl.myparadise.common.mongo.utils;

import com.gl.myparadise.common.mongo.file.MongoExport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Slf4j
public class ExportUtils {

    public static <T> byte[] getExportData(List<T> exportData, boolean isNewDoc, MessageSource messageSource, Locale locale) {
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
                title = getTitle(exportElement.getClass(), messageSource, locale);
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
     * 获取「BOM + 表头 + 换行」的字节内容，用于查询结果为空时仍能输出带完整表头的文件
     */
    public static byte[] getHeaderData(Class<?> classz, MessageSource messageSource, Locale locale) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // BOM标识
            outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            // 表头
            outputStream.write(getTitle(classz, messageSource, locale).getBytes(StandardCharsets.UTF_8));
            outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            log.error("getHeaderData error", ex);
            return null;
        }
    }

    /**
     * 获取要导出数据的表头
     */
    public static <T> String getTitle(Class<T> classz, MessageSource messageSource, Locale locale) {
        StringBuilder titleBuffer = new StringBuilder();
        try {
            Field[] fields = classz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                MongoExport MongoExport = field.getAnnotation(MongoExport.class);
                if (ObjectUtils.isEmpty(MongoExport)) {
                    continue;
                }
                if (messageSource != null && StringUtils.isNotBlank(MongoExport.name())) {
                    titleBuffer.append(messageSource.getMessage(MongoExport.name(), null, MongoExport.name(), locale)).append(",");
                } else {
                    titleBuffer.append(MongoExport.name()).append(",");
                }
            }
        } catch (Exception ex) {
            log.error("get export header title error", ex);
        }
        return StringUtils.removeEnd(titleBuffer.toString(), ",");
    }
}