package com.fitnesspartner.domain;

import com.fitnesspartner.constants.LessonState;
import com.fitnesspartner.dto.lesson.LessonUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column(nullable = false)
    private String lessonName;

    @Column(nullable = false)
    private String lessonDescription;

    @Column(nullable = false)
    private int maxEnrollment;

    @Column(nullable = false)
    private String centerName;

    @Column(nullable = false)
    private String centerAddress;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonState lessonState;

//    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructorId")
    private Instructor instructor;

    public void lessonUpdate(LessonUpdateRequestDto requestDto) {
        if(requestDto.getLessonName() != null) {
            this.lessonName = requestDto.getLessonName();
        }

        if(requestDto.getLessonDescription() != null) {
            this.lessonDescription = requestDto.getLessonDescription();
        }

        if(requestDto.getCenterName() != null) {
            this.centerName = requestDto.getCenterName();
        }

        if(requestDto.getMaxEnrollment() != null) {
            this.maxEnrollment = requestDto.getMaxEnrollment();
        }

        if(requestDto.getCenterAddress() != null) {
            this.centerAddress = requestDto.getCenterAddress();
        }

        if(requestDto.getStartDateTime() != null) {
            this.startDateTime = requestDto.getStartDateTime();
        }

        if(requestDto.getEndDateTime() != null) {
            this.endDateTime = requestDto.getEndDateTime();
        }
    }

    public void lessonDisable() {
        this.lessonState = LessonState.Disabled;
    }
}
