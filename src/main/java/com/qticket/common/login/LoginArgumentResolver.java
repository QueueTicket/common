package com.qticket.common.login;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class) && parameter.getParameterType().equals(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String userIdHeader = webRequest.getHeader("X-USER-ID");
        String userRoleHeader = webRequest.getHeader("X-USER-ROLE");

        // X-USER-ID가 null일 경우 처리
        Long currentUserId = null;
        if (userIdHeader != null && !userIdHeader.isEmpty()) {
            currentUserId = Long.parseLong(userIdHeader);
        }

        String currentUserRole = (userRoleHeader != null) ? userRoleHeader : null;

        return new CurrentUser(currentUserId, currentUserRole);
    }
}