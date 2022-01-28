package com.agilework.sims.config;

import com.agilework.sims.service.SessionService;
import com.agilework.sims.util.SLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    private static final String TAG = "LoginInterceptor";

    @Autowired
    private SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String sessionId = request.getHeader("sessionId");
        if (sessionService.getSession(sessionId) == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            SLogger.info(TAG, "reject unknown session, url="
                    + request.getRequestURI() + ", sessionId=" + sessionId);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
