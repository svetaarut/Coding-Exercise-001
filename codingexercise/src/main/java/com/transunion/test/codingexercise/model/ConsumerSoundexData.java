package com.transunion.test.codingexercise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsumerSoundexData {
    private String name;
    private String soundexNameCode;
    private Integer soundexIntNameCode;
    private String groupingCode;
}
