package com.transunion.test.codingexercise.service;

import com.transunion.test.codingexercise.model.Bucket;
import com.transunion.test.codingexercise.model.ConsumerSoundexData;
import com.transunion.test.codingexercise.model.Statistics;
import java.util.List;
import java.util.Map;

public interface DataProviderService {

    Statistics getStatistics();

    Bucket getBucketDataByColumnName(final String columnName) ;

    Map<String, List<ConsumerSoundexData> > getSoundexMapByColumnName(String columnName);
}
