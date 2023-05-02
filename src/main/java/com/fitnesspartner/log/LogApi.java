package com.fitnesspartner.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "log_api")
public class LogApi {

    @Id
    private String logApiId;

    private int status;

    private String method;

    private String handlerMethodName;

    private String servletPath;

    private String requestBody;

    private String responseBody;

    private String queryUrl;

    private LocalDateTime localDateTime;
}
