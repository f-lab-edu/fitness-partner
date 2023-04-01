package com.fitnesspartner.controller;

import com.fitnesspartner.dto.lesson.LessonCreateRequestDto;
import com.fitnesspartner.dto.lesson.LessonDisableRequestDto;
import com.fitnesspartner.dto.lesson.LessonInfoResponseDto;
import com.fitnesspartner.dto.lesson.LessonUpdateRequestDto;
import com.fitnesspartner.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping()
    public ResponseEntity<String> lessonCreate(@Valid LessonCreateRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(lessonService.lessonCreate(requestDto));
    }

    @DeleteMapping()
    public ResponseEntity<String> lessonDisable(@RequestBody LessonDisableRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(lessonService.lessonDisable(requestDto));
    }

    @PutMapping()
    public ResponseEntity<String> lessonUpdate(@RequestBody LessonUpdateRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(lessonService.lessonUpdate(requestDto));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonInfoResponseDto> lessonInfo(@PathVariable Long lessonId) {
        return ResponseEntity.ok()
                .body(lessonService.lessonInfo(lessonId));
    }
}
