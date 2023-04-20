package com.fitnesspartner.controller;

import com.fitnesspartner.dto.lessonMember.LessonMemberResponseDto;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import com.fitnesspartner.service.LessonMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonMemberController {
    private final LessonMemberService lessonMemberService;

    @PostMapping("/member")
    public ResponseEntity<LessonMemberResponseDto> switchToLessonMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .body(lessonMemberService.switchToLessonMember(userDetails));
    }

    @GetMapping("/member/{lessonMemberId}")
    public ResponseEntity<LessonMemberResponseDto> lessonMemberInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PathVariable Long lessonMemberId) {
        return ResponseEntity.ok()
                .body(lessonMemberService.lessonMemberInfo(userDetails, lessonMemberId));
    }
}
