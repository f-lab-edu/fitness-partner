package com.fitnesspartner.service;


import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Instructor;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.instructor.InstructorAddressUpdateRequestDto;
import com.fitnesspartner.dto.instructor.InstructorInfoResponseDto;
import com.fitnesspartner.dto.instructor.SwitchToInstructorRequestDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.InstructorRepository;
import com.fitnesspartner.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final UsersRepository usersRepository;

    private final InstructorRepository instructorRepository;

    public String switchToInstructor(SwitchToInstructorRequestDto switchToInstructorRequestDto) {
        String username = switchToInstructorRequestDto.getUsername();

        Users users = findUserByUsername(username);
        instructorExistsCheckByUsers(users);

        Instructor instructor = Instructor.builder()
                .users(users)
                .addressSido(switchToInstructorRequestDto.getAddressSido())
                .addressSigungu(switchToInstructorRequestDto.getAddressSigungu())
                .addressRoadName(switchToInstructorRequestDto.getAddressRoadName())
                .addressDetails(switchToInstructorRequestDto.getAddressDetails())
                .instructorState(InstructorState.Enabled)
                .build();

        instructorRepository.save(instructor);

        return "강사 전환이 완료되었습니다.";
    }

    public InstructorInfoResponseDto instructorInfo(String username) {

        Users users = findUserByUsername(username);

        Instructor instructor = findExistsInstructorByUsers(users);

        return InstructorInfoResponseDto.builder()
                .instructorNickname(users.getNickname())
                .addressSido(instructor.getAddressSido())
                .addressSigungu(instructor.getAddressSigungu())
                .addressRoadName(instructor.getAddressRoadName())
                .addressDetails(instructor.getAddressDetails())
                .build();
    }

    @Transactional
    public String instructorAddressUpdate(InstructorAddressUpdateRequestDto instructorAddressUpdateRequestDto) {

        Users users = findUserByUsername(instructorAddressUpdateRequestDto.getUsername());
        Instructor instructor = instructorRepository.findByUsersAndInstructorState(users, InstructorState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_INSTRUCTOR)
                );

        instructor.instructorAddressUpdate(instructorAddressUpdateRequestDto);

        return "강사 활동지역 주소를 업데이트 했습니다.";
    }

    public Object addInstructorCertificate() {
        return new Object();
    }


    private Instructor findExistsInstructorByUsers(Users users) {
        return instructorRepository.findByUsersAndInstructorState(users, InstructorState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_INSTRUCTOR)
                );
    }

    private void instructorExistsCheckByUsers(Users users) {
        if(instructorRepository.existsByUsers(users)) {
            throw new RestApiException(ClientExceptionCode.USER_ALREADY_INSTRUCTOR);
        }
    }

    private Users findUserByUsername(String username) {
        return usersRepository.findByUsernameAndUserState(username, UserState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_USER)
                );
    }
}