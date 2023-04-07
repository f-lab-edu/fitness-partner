package com.fitnesspartner.domain;

import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.dto.instructor.InstructorAddressUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usersId")
    private Users users;

    public void instructorAddressUpdate(InstructorAddressUpdateRequestDto instructorAddressUpdateRequestDto) {
        this.addressSido = instructorAddressUpdateRequestDto.getAddressSido();
        this.addressSigungu = instructorAddressUpdateRequestDto.getAddressSigungu();
        this.addressRoadName = instructorAddressUpdateRequestDto.getAddressRoadName();

        String addressDetails = instructorAddressUpdateRequestDto.getAddressDetails();
        if(addressDetails != null) {
            this.addressDetails = addressDetails;
        }
    }
}
