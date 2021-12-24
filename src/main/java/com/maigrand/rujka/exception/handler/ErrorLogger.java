package com.maigrand.rujka.exception.handler;

import com.maigrand.rujka.exception.ErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ErrorLogger implements ApplicationListener<ErrorEvent> {

    @Override
    public void onApplicationEvent(ErrorEvent event) {
        Throwable throwable = event.getThrowable();
        log.error(throwable.getMessage(), throwable);
    }
}
