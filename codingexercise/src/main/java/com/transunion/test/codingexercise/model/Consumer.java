package com.transunion.test.codingexercise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Consumer {

    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Integer vantageScore;
    private Integer ficoScore;
    private String soundexFirstNameCode;
    private String soundexLastNameCode;
    private String soundexEmailCode;

}
