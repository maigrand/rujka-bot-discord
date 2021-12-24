package com.maigrand.rujka.payload;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
public class ErrorDetails {

    protected final String timestamp;

    protected final int status;

    protected final String error;

    protected final Object message;

    protected final String path;

    public ErrorDetails(HttpStatus status, Collection<String> messages, String path) {
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = messages.size() == 1
                ? messages.stream().findFirst().get()
                : messages;
        this.path = path;
    }
}
