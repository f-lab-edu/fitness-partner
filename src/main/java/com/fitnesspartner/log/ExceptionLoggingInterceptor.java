package com.fitnesspartner.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionLoggingInterceptor implements AsyncHandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ExceptionLoggingInterceptor.class);

    private final LogApiRepository logApiRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        int currentStatus = response.getStatus();
        String method = request.getMethod();

        if(!method.equals("GET") && currentStatus == 400 || currentStatus == 500) {
            String servletPath = request.getServletPath();
            String handlerMethodName = getHandlerMethodName(handler);


            logger.error("Status : {}", currentStatus);
            logger.error("Method : {}", method);
            logger.error("ServletPath : {}", servletPath);
            logger.error("HandlerMethodName : {}",handlerMethodName);

            LogApi logApi = LogApi.builder()
                    .status(currentStatus)
                    .method(method)
                    .handlerMethodName(handlerMethodName)
                    .servletPath(servletPath)
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
