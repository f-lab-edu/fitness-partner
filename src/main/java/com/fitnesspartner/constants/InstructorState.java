package com.fitnesspartner.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstructorState {
    Enabled("활성화"),
    Disabled("비활성화")
    ;

    private String description;
}
