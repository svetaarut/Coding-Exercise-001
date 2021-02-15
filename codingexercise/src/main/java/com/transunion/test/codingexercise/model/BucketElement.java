package com.transunion.test.codingexercise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BucketElement {
    private Integer bucketNumber;
    private String range;
    private Integer count;
}
