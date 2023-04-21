package com.fitnesspartner.controller;

import com.fitnesspartner.dto.lessonBooking.LessonBookingGetAllByUserResponseDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingRemoveRequestDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingResponseDto;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import com.fitnesspartner.service.LessonBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lesson/booking")
@RequiredArgsConstructor
public class LessonBookingController {

    private final LessonBookingService lessonBookingService;

    @PostMapping("/{lessonId}")
    public ResponseEntity<String> lessonBookingCreate(@PathVariable Long lessonId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingCreate(lessonId, userDetails));
    }

    @DeleteMapping()
    public ResponseEntity<String> lessonBookingRemove(@Valid @RequestBody LessonBookingRemoveRequestDto requestDto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingRemove(requestDto, userDetails));
    }

    @GetMapping("/member/{lessonMemberId}")
    public ResponseEntity<LessonBookingGetAllByUserResponseDto> lessonBookingGetAll(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long lessonMemberId
    ) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingGetAll(userDetails, lessonMemberId));
    }

    @GetMapping("/{lessonBookingId}")
    public ResponseEntity<LessonBookingResponseDto> lessonBookingInfo(@PathVariable Long lessonBookingId,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingInfo(lessonBookingId, userDetails));
    }
}
