package com.transunion.test.codingexercise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BucketRange {
    private int bucketNumber;
    private int bucketRangeStart;
    private int bucketRangeEnd;
    private int count;
    String rangeLabel;
}
