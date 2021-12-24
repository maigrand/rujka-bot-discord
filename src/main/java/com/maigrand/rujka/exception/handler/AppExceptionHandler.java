package com.maigrand.rujka.exception.handler;

import com.maigrand.rujka.exception.ErrorEvent;
import com.maigrand.rujka.payload.ErrorDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(
            Exception e,
            WebRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof AuthenticationException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (e instanceof AccessDeniedException) {
            httpStatus = HttpStatus.FORBIDDEN;
        }

        Collection<String> messages = getMessages(e);
        if (httpStatus == INTERNAL_SERVER_ERROR) {
            this.eventPublisher.publishEvent(new ErrorEvent(this, e));
        }

        return handleExceptionInternal(e, messages, httpStatus, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected final ResponseEntity<Object> handleResponseStatusException(
            ResponseStatusException exc,
            WebRequest request) {
        Collection<String> messages = getMessages(exc);
        HttpStatus httpStatus = exc.getStatus();
        return handleExceptionInternal(exc, messages, httpStatus, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception exc,
            @NonNull Collection<String> messages,
            @NonNull HttpStatus httpStatus,
            @NonNull WebRequest request) {
        ErrorDetails error = createErrorDetails(httpStatus, messages, request);
        return handleExceptionInternal(exc, error, new HttpHeaders(), httpStatus, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception exc,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus httpStatus,
            @NonNull WebRequest request) {
        if (!(body instanceof ErrorDetails)) {
            Collection<String> messages = getMessages(exc);
            body = createErrorDetails(httpStatus, messages, request);
        }

        return new ResponseEntity<>(body, httpStatus);
    }

    protected Collection<String> getMessages(Throwable exc) {
        String message = exc instanceof ResponseStatusException
                ? ((ResponseStatusException) exc).getReason()
                : exc.getMessage();
        if (StringUtils.isBlank(message)) {
            String exceptionName = exc.getClass().getName();
            StackTraceElement[] stackTrace = exc.getStackTrace();
            message = String.format("%s: %s", exceptionName, stackTrace[0]);
        }
        return Collections.singleton(message);
    }

    protected ErrorDetails createErrorDetails(
            HttpStatus httpStatus,
            Collection<String> messages,
            WebRequest request) {
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ErrorDetails(httpStatus, messages, requestURI);
    }
}
