package com.fitnesspartner.controller;

import com.fitnesspartner.dto.instructor.*;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import com.fitnesspartner.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @PostMapping()
    public ResponseEntity<SwitchToInstructorResponseDto> switchToInstructor(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @Valid @RequestBody SwitchToInstructorRequestDto switchToInstructorRequestDto) {
        return ResponseEntity.ok()
                .body(instructorService.switchToInstructor(userDetails, switchToInstructorRequestDto));
    }

    @GetMapping("/{username}")
    public ResponseEntity<InstructorInfoResponseDto> instructorInfo(@PathVariable String username) {
        return ResponseEntity.ok()
                .body(instructorService.instructorInfo(username));
    }

    @PutMapping("/address")
    public ResponseEntity<String> instructorAddressUpdate(@Valid @RequestBody InstructorAddressUpdateRequestDto instructorAddressUpdateRequestDto) {
        return ResponseEntity.ok()
                .body(instructorService.instructorAddressUpdate(instructorAddressUpdateRequestDto));
    }

    @PostMapping("/certificate")
    public ResponseEntity<Object> addInstructorCertificate() {
        return ResponseEntity.ok()
                .body(instructorService.addInstructorCertificate());
    }

    @GetMapping("/lessons")
    public ResponseEntity<InstructorLessonsResponseDto> instructorLessons(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .body(instructorService.getInstructorLessons(userDetails));
    }
}
