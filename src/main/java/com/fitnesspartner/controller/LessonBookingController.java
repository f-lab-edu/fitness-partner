package com.fitnesspartner.controller;

import com.fitnesspartner.dto.lessonBooking.LessonBookingCreateRequestDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingGetAllByUserResponseDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingRemoveRequestDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingResponseDto;
import com.fitnesspartner.service.LessonBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lesson/booking")
@RequiredArgsConstructor
public class LessonBookingController {

    private final LessonBookingService lessonBookingService;

    @PostMapping()
    public ResponseEntity<String> lessonBookingCreate(@Valid @RequestBody LessonBookingCreateRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingCreate(requestDto));
    }

    @DeleteMapping()
    public ResponseEntity<String> lessonBookingRemove(@Valid @RequestBody LessonBookingRemoveRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingRemove(requestDto));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<LessonBookingGetAllByUserResponseDto> lessonBookingGetAllByUser(@PathVariable String username) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingGetAllByUser(username));
    }

    @GetMapping("/{lessonBookingId}")
    public ResponseEntity<LessonBookingResponseDto> lessonBookingInfo(@PathVariable Long lessonBookingId) {
        return ResponseEntity.ok()
                .body(lessonBookingService.lessonBookingInfo(lessonBookingId));
    }
}
