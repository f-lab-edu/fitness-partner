package com.fitnesspartner.controller;

import com.fitnesspartner.dto.instructor.InstructorAddressUpdateRequestDto;
import com.fitnesspartner.dto.instructor.InstructorInfoResponseDto;
import com.fitnesspartner.dto.instructor.SwitchToInstructorRequestDto;
import com.fitnesspartner.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @PostMapping()
    public ResponseEntity<String> switchToInstructor(@Valid @RequestBody SwitchToInstructorRequestDto switchToInstructorRequestDto) {
        return ResponseEntity.ok()
                .body(instructorService.switchToInstructor(switchToInstructorRequestDto));
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
}
