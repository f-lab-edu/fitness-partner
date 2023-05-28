package com.fitnesspartner.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonMemberId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    @OneToMany(mappedBy = "lessonMember", fetch = FetchType.LAZY)
    private final List<LessonBooking> lessonBookingList = new ArrayList<>();
}
