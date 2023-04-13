package com.fitnesspartner.controller;

import com.fitnesspartner.dto.users.*;
import com.fitnesspartner.jwt.JwtToken;
import com.fitnesspartner.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<String> userSignup(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {
        return ResponseEntity.ok()
                .body(usersService.userSignup(userSignupRequestDto));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> userInfo(@PathVariable String username) {
        return ResponseEntity.ok()
                .body(usersService.userInfo(username));
    }

    @PutMapping("{username}")
    public ResponseEntity<UserResponseDto> userInfoUpdate(@PathVariable String username,
                                                          @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok()
                .body(usersService.userUpdate(username, userUpdateRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@Valid @RequestBody UserLoginRequestDto requestDto, HttpServletResponse response) {
        String tokenValue = usersService.userLogin(requestDto);
        Cookie cookie = new Cookie(JwtToken.TOKEN_NAME.getTokenName(), tokenValue);
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        return ResponseEntity.ok()
                .body(tokenValue);
    }

    @DeleteMapping()
    public ResponseEntity<String> userDisable(@Valid @RequestBody UserDisableRequestDto userDisableRequestDto) {
        return ResponseEntity.ok()
                .body(usersService.userDisable(userDisableRequestDto));
    }
}
