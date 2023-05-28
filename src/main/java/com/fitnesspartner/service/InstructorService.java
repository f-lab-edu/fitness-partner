package com.fitnesspartner.service;


import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.constants.LessonState;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.*;
import com.fitnesspartner.dto.instructor.*;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.InstructorRepository;
import com.fitnesspartner.repository.UserRolesRepository;
import com.fitnesspartner.repository.UsersRepository;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.fitnesspartner.security.Roles.INSTRUCTOR;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final UsersRepository usersRepository;

    private final InstructorRepository instructorRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final UserRolesRepository userRolesRepository;

    public SwitchToInstructorResponseDto switchToInstructor(CustomUserDetails userDetails,
                                                            SwitchToInstructorRequestDto switchToInstructorRequestDto) {

        Users user = userDetails.getUsers();

        Instructor instructor = instructorRepository.findByUsersAndInstructorState(user, InstructorState.Enabled)
                .orElseGet(
                        () -> saveInstructor(user, switchToInstructorRequestDto)
                );

        return SwitchToInstructorResponseDto.builder()
                .instructorName(user.getName())
                .instructorUsername(user.getUsername())
                .addressSido(instructor.getAddressSido())
                .addressSigungu(instructor.getAddressSigungu())
                .addressRoadName(instructor.getAddressRoadName())
                .addressDetails(instructor.getAddressDetails())
                .build();
    }

    private Instructor saveInstructor(Users user, SwitchToInstructorRequestDto switchToInstructorRequestDto) {
        Instructor instructor = Instructor.builder()
                .users(user)
                .addressSido(switchToInstructorRequestDto.getAddressSido())
                .addressSigungu(switchToInstructorRequestDto.getAddressSigungu())
                .addressRoadName(switchToInstructorRequestDto.getAddressRoadName())
                .addressDetails(switchToInstructorRequestDto.getAddressDetails())
                .instructorState(InstructorState.Enabled)
                .build();

        instructorRepository.save(instructor);

        UserRoles userRoles = UserRoles.builder()
                .users(user)
                .roleName(INSTRUCTOR.getRoleName())
                .build();

        userRolesRepository.save(userRoles);

        return instructor;
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

    public InstructorLessonsResponseDto getInstructorLessons(CustomUserDetails userDetails) {
        Users user = userDetails.getUsers();

        QInstructor qInstructor = QInstructor.instructor;
        QLesson qLesson = QLesson.lesson;

        List<Lesson> lessonList = jpaQueryFactory.selectFrom(qLesson)
                .innerJoin(qLesson.instructor, qInstructor).fetchJoin()
                .where(qLesson.instructor.users.usersId.eq(user.getUsersId())
                        .and(qLesson.lessonState.eq(LessonState.Enabled))
                )
                .fetch();

        List<InstructorLessonInfo> instructorLessonInfoList = new ArrayList<>();

        for(Lesson lesson : lessonList) {
            InstructorLessonInfo lessonResponseDto = InstructorLessonInfo.builder()
                    .username(user.getUsername())
                    .lessonName(lesson.getLessonName())
                    .maxEnrollment(lesson.getMaxEnrollment())
                    .centerName(lesson.getCenterName())
                    .centerAddress(lesson.getCenterAddress())
                    .startDateTime(lesson.getStartDateTime())
                    .endDateTime(lesson.getEndDateTime())
                    .build();

            instructorLessonInfoList.add(lessonResponseDto);
        }

        return InstructorLessonsResponseDto.builder()
                .instructorLessonInfoList(instructorLessonInfoList)
                .build();
    }

    private Instructor findExistsInstructorByUsers(Users users) {
        return instructorRepository.findByUsersAndInstructorState(users, InstructorState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_INSTRUCTOR)
                );
    }

    private Users findUserByUsername(String username) {
        return usersRepository.findByUsernameAndUserState(username, UserState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_USER)
                );
    }
}