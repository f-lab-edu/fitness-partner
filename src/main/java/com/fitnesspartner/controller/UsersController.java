package com.fitnesspartner.controller;

import com.fitnesspartner.dto.users.*;
import com.fitnesspartner.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<String> userSignup(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersService.userSignup(userSignupRequestDto));
    }

    // TODO: 2023-03-19  : Spring Security와 JWT로 유저 로그인 기능 구현
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> userLogin(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return ResponseEntity.ok()
                .body(usersService.userLogin(userLoginRequestDto));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> userInfo(@PathVariable String username) {
        return ResponseEntity.ok()
                .body(usersService.userInfo(username));
    }

    // TODO: 2023-03-19  : QueryDSL 적용 후 동적 쿼리로 유저 정보 수정
    @PutMapping("{username}")
    public ResponseEntity<UserResponseDto> userInfoUpdate(@PathVariable String username,
                                                          @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok()
                .body(usersService.userUpdate(username, userUpdateRequestDto));
    }

    @DeleteMapping()
    public ResponseEntity<String> userDisable(@Valid @RequestBody UserDisableRequestDto userDisableRequestDto) {
        return ResponseEntity.ok()
                .body(usersService.userDisable(userDisableRequestDto));
    }
}
