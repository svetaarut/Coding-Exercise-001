package com.transunion.test.codingexercise.service;

import com.transunion.test.codingexercise.model.Bucket;
import com.transunion.test.codingexercise.model.SoundexResponse;
import com.transunion.test.codingexercise.model.Statistics;

public interface ConsumerService {

    Statistics getStatistics();

    Bucket getBucketDataByColumnName(final String columnName) ;

    SoundexResponse getMatchByColumnName(String columnName, String name);
}
