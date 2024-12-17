package com.gl.myparadise.common.mongo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

@Data
public class RemainingTimeUtils {

    /**
     * 計算剩下的時間
     */
    public static <T> ProcessVO getRemainingTimeInfo(long startTime, long currentTime, long totalProcessMetrics, long doneProcessMetrics, String objectId, boolean isDone) {
        if (doneProcessMetrics == 0L) {
            return new ProcessVO(objectId, isDone);
        }
        long elapsedTime = currentTime - startTime;
        if (elapsedTime < 0) {
            return new ProcessVO(objectId, isDone);
        }
        long allTimeForProcessing = (elapsedTime * totalProcessMetrics / doneProcessMetrics);
        long remainingTime = allTimeForProcessing - elapsedTime;
        return new ProcessVO(LocalTime.ofNanoOfDay(remainingTime < 0 ? 0 : remainingTime), startTime, currentTime, totalProcessMetrics, doneProcessMetrics, objectId, isDone);
    }

    /**
     * 計算剩下的時間
     */
    public static LocalTime getRemainingTime(long startTime, long currentTime, long totalProcessMetrics, long doneProcessMetrics) {
        if (doneProcessMetrics == 0L) {
            return null;
        }
        long elapsedTime = currentTime - startTime;
        if (elapsedTime < 0) {
            return null;
        }
        long allTimeForProcessing = (elapsedTime * totalProcessMetrics / doneProcessMetrics);
        long remainingTime = allTimeForProcessing - elapsedTime;

        return LocalTime.ofNanoOfDay(remainingTime < 0 ? 0 : remainingTime);
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class ProcessVO {
        private LocalTime remainingTime;
        private long startTime;
        private long currentTime;
        private long totalProcessMetrics;
        private long doneProcessMetrics;
        @NonNull
        private String objectId;
        @NonNull
        private boolean isDone;

        public BigDecimal getProcess() {
            if (totalProcessMetrics == 0 || doneProcessMetrics == 0) {
                return BigDecimal.ZERO;
            }
            return BigDecimal.valueOf(doneProcessMetrics).divide(BigDecimal.valueOf(totalProcessMetrics), 2, RoundingMode.HALF_UP);
        }
    }
}
