package com.fitnesspartner.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionLoggingInterceptor implements AsyncHandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ExceptionLoggingInterceptor.class);

    private final LogApiRepository logApiRepository;

    private final ObjectMapper objectMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        int currentStatus = response.getStatus();
        String method = request.getMethod();

        if(!method.equals("GET") &&  !(currentStatus <= 200 && currentStatus < 300) ) {
            final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
            final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

            String servletPath = cachingRequest.getServletPath();
            String handlerMethodName = getHandlerMethodName(handler);
            String requestBody = String.valueOf(objectMapper.readTree(cachingRequest.getContentAsByteArray()));
            String responseBody = String.valueOf(objectMapper.readTree(cachingResponse.getContentAsByteArray()));
            String queryUrl = cachingRequest.getQueryString();

            logger.error("Status : {}", currentStatus);
            logger.error("Method : {}", method);
            logger.error("ServletPath : {}", servletPath);
            logger.error("HandlerMethodName : {}",handlerMethodName);
            logger.error("Request Body : {}", requestBody);
            logger.error("Response Body : {}", responseBody);
            logger.error("Query URL : {}", queryUrl);


            LogApi logApi = LogApi.builder()
                    .status(currentStatus)
                    .method(method)
                    .handlerMethodName(handlerMethodName)
                    .servletPath(servletPath)
                    .requestBody(requestBody)
                    .responseBody(responseBody)
                    .queryUrl(queryUrl)
                    .localDateTime(LocalDateTime.now())
                    .build();

            logApiRepository.save(logApi);
        }
    }

    private String getHandlerMethodName(Object handler) {
        String handlerString = handler.toString();
        String handlerMethodName = handlerString.substring(handlerString.lastIndexOf('.') + 1);

        return handlerMethodName;
    }
}
