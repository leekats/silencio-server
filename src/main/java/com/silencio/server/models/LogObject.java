package com.silencio.server.models;

import lombok.Builder;
import lombok.Value;

@Value
public class LogObject {
    private long timestamp;
    private String objectId;
    private String userMessage;
    private String additionalTrackingId;
    private String exceptionMessage;
    private StackTraceElement[] stackTrace;

    @Builder
    LogObject(String objectId, String userMessage, String additionalTrackingId, String exceptionMessage, StackTraceElement[] stackTrace) {
        this.timestamp = System.currentTimeMillis();
        this.objectId = objectId;
        this.userMessage = userMessage;
        this.additionalTrackingId = additionalTrackingId;
        this.exceptionMessage = exceptionMessage;
        this.stackTrace = stackTrace;
    }
}