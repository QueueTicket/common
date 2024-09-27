import com.qticket.common.dto.ResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 모든 응답에 대해 적용할 수 있게 true를 반환합니다.
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
        // 이미 ResponseDto로 래핑된 응답은 다시 래핑하지 않도록 예외 처리
        if (body instanceof ResponseDto) {
            return body;
        }

        // 예외적으로 에러 응답 처리
        if (response.getStatusCode() != null && !response.getStatusCode().is2xxSuccessful()) {
            return ResponseDto.error(response.getStatusCode().name(), body.toString());
        }

        // 성공적인 응답의 경우 ResponseDto로 래핑
        return ResponseDto.success(HttpStatus.OK.name(), body);
    }
}