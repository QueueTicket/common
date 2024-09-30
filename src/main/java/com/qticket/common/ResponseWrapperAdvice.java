package com.qticket.common;

import com.qticket.common.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private final HttpServletResponse httpServletResponse;

    public ResponseWrapperAdvice(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        // HttpServletResponse를 통해 상태 코드 확인
        int statusCode = httpServletResponse.getStatus();

        // 이미 ResponseDto로 래핑된 응답은 다시 래핑하지 않도록 예외 처리
        if (body instanceof ResponseDto) {
            return body;
        }

        // 에러 응답 처리
        if (statusCode >= 400) {
            return ResponseDto.error(body.toString());
        }

        // 성공적인 응답의 경우 ResponseDto로 래핑
        return ResponseDto.success(HttpStatus.OK.name(), body);
    }
}