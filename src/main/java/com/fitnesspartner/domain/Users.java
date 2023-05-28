package com.fitnesspartner.domain;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.dto.users.UserUpdateRequestDto;
import com.fitnesspartner.utils.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Users extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usersId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState userState;

    @OneToMany(mappedBy =  "users", fetch = FetchType.LAZY)
    private List<UserRoles> userRolesList = new ArrayList<>();

    public void updateUser(UserUpdateRequestDto userUpdateRequestDto) {

        if(userUpdateRequestDto.getName() != null) {
            this.name = userUpdateRequestDto.getName();
        }

        if(userUpdateRequestDto.getEmail() != null) {
            this.email = userUpdateRequestDto.getEmail();
        }

        if(userUpdateRequestDto.getGender() != null) {
            this.gender = userUpdateRequestDto.getGender();
        }

        if(userUpdateRequestDto.getPhoneNumber()!= null) {
            this.phoneNumber = userUpdateRequestDto.getPhoneNumber();
        }

        if(userUpdateRequestDto.getNickname() != null) {
            this.nickname = userUpdateRequestDto.getNickname();
        }
    }

    public void userDisable(UserState userState) {
        this.userState = userState;
    }
}
