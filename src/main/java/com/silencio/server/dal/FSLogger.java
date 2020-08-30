package com.silencio.server.dal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silencio.server.models.LogObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class FSLogger {
    private static final Logger log4jLogger = LogManager.getLogger(FSLogger.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public static FSLogger getInstance() {
        return new FSLogger();
    }

    private void info(LogObject logObject) {
        logObjectToJson(logObject).ifPresent(log4jLogger::info);
    }

    public void error(Throwable t) {
        logObjectToJson(LogObject.builder()
                .exceptionMessage(t.getMessage())
                .stackTrace(t.getStackTrace())
                .build())
                .ifPresent(log4jLogger::error);
    }

    private Optional<String> logObjectToJson(LogObject logObject) {
        try {
            return Optional.ofNullable(objectMapper.writeValueAsString(logObject));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}