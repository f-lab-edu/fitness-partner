package com.fitnesspartner.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesspartner.dto.users.UserResponseDto;
import com.fitnesspartner.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private static final String AUTH_HEADER = "Authorization ";
    private static final String TOKEN_TYPE = "BEARER ";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "application/json";

    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtService jwtService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 전달받은 인증정보 SecurityContextHolder에 저장
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // JWT Token 발급
        final String token = jwtService.generateToken(userDetails);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);

        UserResponseDto responseDto = UserResponseDto.builder()
                .username(userDetails.getUsername())
                .build();

        String result = mapper.writeValueAsString(responseDto);
        response.getWriter().write(result);
    }
}
