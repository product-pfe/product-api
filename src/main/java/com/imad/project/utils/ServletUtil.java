package com.imad.project.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ServletUtil implements HandlerInterceptor {

    private static final ThreadLocal<HttpServletRequest> REQUEST_TL = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = REQUEST_TL.get();
        if (request == null) {
            throw new IllegalArgumentException("No request found");
        }
        System.out.println("getRequest() in thread: " + Thread.currentThread().getName());
        return request;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("preHandle() in thread: " + Thread.currentThread().getName());
        REQUEST_TL.set(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        System.out.println("postHandle() remove from thread: " + Thread.currentThread().getName());
        // remove in postHandle (comme dans ton code Kotlin)
        REQUEST_TL.remove();
    }

    // Optional: also remove in afterCompletion to be safe (in case of exceptions)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        REQUEST_TL.remove();
    }
}