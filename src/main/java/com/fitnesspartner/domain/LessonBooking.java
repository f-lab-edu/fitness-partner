package com.fitnesspartner.domain;

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
public class LessonBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonBookingId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lessonId", nullable = false)
    private Lesson lesson;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usersId", nullable = false)
    private Users users;
}
