package cab.aggregator.app.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class HttpLoggingAspect {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String RESPONSE_MESSAGE = """
                    Response:
                    Execution Time = {} ms
                    Body = {}""";
    private static final String REQUEST_MESSAGE = """
                    Request:
                    Method = {} {}
                    Headers = {}
                    Parameters = {}""";
    private static final String ERROR_MESSAGE = """
                Error processing:
                Error Message = {}""";

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logHttpRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logRequest(request);

        long startTime = System.currentTimeMillis();
        Object response;
        try {
            response = joinPoint.proceed();
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage());
            throw e;
        }
        long executionTime = System.currentTimeMillis() - startTime;

        logResponse(response, executionTime);
        return response;
    }

    private void logRequest(HttpServletRequest request) {
        if (log.isInfoEnabled()) {
            log.info(REQUEST_MESSAGE,
                    request.getMethod(),
                    request.getRequestURI(),
                    getHeadersInfo(request),
                    getParametersInfo(request));
        }
    }

    private String getHeadersInfo(HttpServletRequest request) {
        String headers = Collections.list(request.getHeaderNames())
                .stream()
                .map(header -> {
                    if (AUTHORIZATION_HEADER.equalsIgnoreCase(header)) {
                        return header + ": [MASKED]";
                    }
                    return header + ": " + request.getHeader(header);
                })
                .collect(Collectors.joining("; "));

        return headers.isEmpty() ? "No Headers" : headers;
    }

    private String getParametersInfo(HttpServletRequest request) {
        String params = request.getParameterMap().entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining("; "));

        return params.isEmpty() ? "No Parameters" : params;
    }

    private void logResponse(Object response, long executionTime) {
        if (log.isInfoEnabled()) {
            log.info(RESPONSE_MESSAGE,
                    executionTime,
                    response);
        }
    }
}
