package cab.aggregator.app.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class HttpLoggingAspect {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String RESPONSE_MESSAGE = """
                    Response for {} {}:
                    Status = {}
                    Execution Time = {} ms
                    Body = {}""";
    private static final String REQUEST_MESSAGE = """
                    Request:
                    Method = {}
                    URL = {}
                    Headers = {}
                    Parameters = {}""";
    private static final String ERROR_MESSAGE = """
                Error processing {} {}:
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
            logError(request, e);
            throw e;
        }
        long executionTime = System.currentTimeMillis() - startTime;

        logResponse(request, response, executionTime, attributes.getResponse());
        return response;
    }

    private void logRequest(HttpServletRequest request) {
        if (log.isInfoEnabled()) {
            log.info(REQUEST_MESSAGE,
                    request.getMethod(),
                    request.getRequestURL(),
                    getFilteredHeaders(request),
                    request.getParameterMap());
        }
    }

    private void logResponse(HttpServletRequest request, Object response, long executionTime, HttpServletResponse servletResponse) {
        if (log.isInfoEnabled()) {
            log.info(RESPONSE_MESSAGE,
                    request.getMethod(),
                    request.getRequestURI(),
                    getStatus(response, servletResponse),
                    executionTime,
                    getResponseBody(response));
        }
    }

    private void logError(HttpServletRequest request, Exception e) {
        log.error(ERROR_MESSAGE,
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage());
    }

    private String getFilteredHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .filter(header -> !header.equalsIgnoreCase(AUTHORIZATION_HEADER))
                .map(header -> header + "=" + request.getHeader(header))
                .collect(Collectors.joining(", "));
    }

    private int getStatus(Object response, HttpServletResponse servletResponse) {
        if (response instanceof ResponseEntity) {
            return ((ResponseEntity<?>) response).getStatusCode().value();
        }
        return servletResponse.getStatus();
    }

    private Object getResponseBody(Object response) {
        if (response instanceof ResponseEntity) {
            return ((ResponseEntity<?>) response).getBody();
        }
        return response;
    }
}
