package ar.edu.itba.paw.exceptions;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class MailAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(final Throwable throwable, final Method method, final Object... obj) {
        System.out.println("[MailServiceAsyncException]");
        System.out.println("Exception message : " + throwable.getMessage());
        System.out.println("Method name : " + method.getName());
        for (final Object param : obj) {
            System.out.println("Param : " + param);
        }
    }

}