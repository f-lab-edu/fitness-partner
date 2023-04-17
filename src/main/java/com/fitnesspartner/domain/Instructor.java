package com.fitnesspartner.domain;

import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.dto.instructor.InstructorAddressUpdateRequestDto;
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
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructorId;

    @Column(nullable = false)
    private String addressSido;

    @Column(nullable = false)
    private String addressSigungu;

    @Column(nullable = false)
    private String addressRoadName;

    private String addressDetails;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InstructorState instructorState;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    @OneToMany(mappedBy = "instructor", fetch =  FetchType.LAZY)
    private final List<Lesson> lessonList = new ArrayList<>();

    public void instructorAddressUpdate(InstructorAddressUpdateRequestDto instructorAddressUpdateRequestDto) {
        this.addressSido = instructorAddressUpdateRequestDto.getAddressSido();
        this.addressSigungu = instructorAddressUpdateRequestDto.getAddressSigungu();
        this.addressRoadName = instructorAddressUpdateRequestDto.getAddressRoadName();

        String addressDetails = instructorAddressUpdateRequestDto.getAddressDetails();
        if(addressDetails != null) {
            this.addressDetails = addressDetails;
        }
    }

    public void addLessonList(Lesson lesson) {
        this.lessonList.add(lesson);
    }
}
