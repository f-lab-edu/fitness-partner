package com.fitnesspartner.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fitnesspartner.dto.common.ExceptionResponseDto;
import com.fitnesspartner.exception.JwtExceptionCode;
import com.fitnesspartner.security.authentication.CustomUserDetailsService;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "application/json";


    // 토큰 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if(servletPath.contains("/users/signup") || servletPath.contains("/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken;
        final String username;

        jwtToken = getJwtToken(request.getCookies());
        if(Strings.isNullOrEmpty(jwtToken)) {
            exceptionResponse(response, JwtExceptionCode.TOKEN_NULL_OR_EMPTY);
            return;
        }

        username = jwtService.extractUsername(jwtToken);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)  {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if(!jwtService.isTokenValid(jwtToken, userDetails)) {
                exceptionResponse(response, JwtExceptionCode.INVALID_TOKEN);
                return;
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request) );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtToken(Cookie[] cookies) {
        if(cookies == null) return null;

        String token = null;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(JwtToken.TOKEN_NAME.getTokenName())) {
                token = cookie.getValue();
            }
        }

        return token;
    }

    private void exceptionResponse(HttpServletResponse response, JwtExceptionCode jwtExceptionCode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);
        response.setStatus(jwtExceptionCode.getCode());

        JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        mapper.registerModule(javaTimeModule);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        ExceptionResponseDto exceptionResponseDto = ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .message(jwtExceptionCode.getMessage())
                        .code(jwtExceptionCode.getCode())
                        .build();

        String result = mapper.writeValueAsString(exceptionResponseDto);
        response.getWriter().write(result);
    }
}
