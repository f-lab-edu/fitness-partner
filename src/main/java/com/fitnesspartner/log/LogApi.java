package com.fitnesspartner.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Document(collation = "log_api")
public class LogApi {

    @Id
    private String logApiId;

    private int status;

    private String method;

    private String handlerMethodName;

    private String servletPath;

    private LocalDateTime localDateTime;
}
