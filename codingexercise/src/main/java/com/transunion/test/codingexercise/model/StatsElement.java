package com.transunion.test.codingexercise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StatsElement {
    private String columnName;
    private Long count;
    private Long distinctCount;
    private Double min;
    private Double max;
    private Double mean;
    private Double stddev;
}
