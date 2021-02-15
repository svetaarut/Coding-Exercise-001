package com.transunion.test.codingexercise.service;

import com.google.common.math.Stats;
import com.transunion.test.codingexercise.config.AppConfig;
import com.transunion.test.codingexercise.constants.Constants;
import com.transunion.test.codingexercise.model.*;
import com.transunion.test.codingexercise.utils.ReaderUtility;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DataProviderServiceImpl implements DataProviderService{

    private Statistics statistics;
    private Map<String, Bucket> bucketMap;
    private Map<String, Map<String, List<ConsumerSoundexData> > > soundexDataMap; //column To Map of first letter to the list Of ConsumerSoundexData

    @Autowired
    AppConfig appConfig;

    @PostConstruct
    public void init() throws IOException {
        List<Consumer> consumers = ReaderUtility.getConsumerData(appConfig.getFileName());
        populateStatsAndBuckets(consumers);
        buildSoundexDataMap(consumers);
    }

    @Override
    public Statistics getStatistics() {
        return this.statistics;
    }

    @Override
    public Bucket getBucketDataByColumnName(String columnName) {
        return this.bucketMap.get(columnName.toUpperCase());
    }

    @Override
    public Map<String, List<ConsumerSoundexData> > getSoundexMapByColumnName(String columnName) {
        return this.soundexDataMap.get(columnName.toUpperCase());
    }

    private void buildSoundexDataMap(List<Consumer> consumers) {
        this.soundexDataMap = new HashMap<>();
        this.soundexDataMap.put(Constants.FIRSTNAME, getFirstNameData(consumers).stream()
                .collect(groupingBy(ConsumerSoundexData::getGroupingCode)));
        this.soundexDataMap.put(Constants.LASTNAME, getLastNameData(consumers).stream()
                .collect(groupingBy(ConsumerSoundexData::getGroupingCode)));
        this.soundexDataMap.put(Constants.EMAIL, getEmailData(consumers).stream()
                .collect(groupingBy(ConsumerSoundexData::getGroupingCode)));
    }

    private List<ConsumerSoundexData> getEmailData(List<Consumer> consumers) {
        return consumers.stream()
                .map(e->ConsumerSoundexData.builder()
                        .name(e.getEmail())
                        .soundexNameCode(e.getSoundexEmailCode())
                        .soundexIntNameCode(getSoundexIntCode(e.getSoundexEmailCode()))
                        .groupingCode(e.getSoundexEmailCode().substring(0,1))
                        .build()
                ).collect(Collectors.toList());
    }

    private  List<ConsumerSoundexData> getLastNameData(List<Consumer> consumers) {
        return consumers.stream()
                .map(e->ConsumerSoundexData.builder()
                        .name(e.getLastName())
                        .soundexNameCode(e.getSoundexLastNameCode())
                        .soundexIntNameCode(getSoundexIntCode(e.getSoundexLastNameCode()))
                        .groupingCode(e.getSoundexLastNameCode().substring(0,1))
                        .build()
                ).collect(Collectors.toList());
    }

    private List<ConsumerSoundexData> getFirstNameData(List<Consumer> consumers) {
        return consumers.stream()
                .map(e->ConsumerSoundexData.builder()
                        .name(e.getFirstName())
                        .soundexNameCode(e.getSoundexFirstNameCode())
                        .soundexIntNameCode(getSoundexIntCode(e.getSoundexFirstNameCode()))
                        .groupingCode(e.getSoundexFirstNameCode().substring(0,1))
                        .build()
                ).collect(Collectors.toList());
    }

    private Integer getSoundexIntCode(String soundexCode) {
       return Integer.valueOf(soundexCode.substring(1));
    }

    private void populateStatsAndBuckets(List<Consumer> consumers) {
        List<StatsElement> stats = new ArrayList<>();
        this.bucketMap = new HashMap<>();
        List<Integer> ages = consumers.stream().map(e->e.getAge()).collect(Collectors.toList());
        StatsElement agestats = populateStatisticForColumn(ages, Constants.DISPLAYAGE);
        stats.add(agestats);
        this.bucketMap.put(Constants.AGE, populateBucket(ages, agestats, Constants.DISPLAYAGE));
        List<Integer> vantageScores = consumers.stream().map(e->e.getVantageScore()).collect(Collectors.toList());
        StatsElement vantageScoresStats = populateStatisticForColumn(vantageScores, Constants.DISPLAYVANTAGESCORE);
        stats.add(vantageScoresStats);
        this.bucketMap.put(Constants.VANTAGESCORE, populateBucket(vantageScores, vantageScoresStats, Constants.DISPLAYVANTAGESCORE));

        List<Integer> ficoScores = consumers.stream().map(e->e.getFicoScore()).collect(Collectors.toList());
        StatsElement ficoScoresStats = populateStatisticForColumn(ficoScores, Constants.DISPLAYFICOSCORE);
        stats.add(ficoScoresStats);
        this.bucketMap.put(Constants.FICOSCORE, populateBucket(ficoScores, ficoScoresStats, Constants.DISPLAYFICOSCORE));

        this.statistics = Statistics.builder()
            .statistics(NumericDistributions.builder()
                    .numericDistributions(stats)
                    .build())
            .build();
    }

    private StatsElement populateStatisticForColumn(List<Integer> columnData, String displayColumnName){
        Stats columnStats = Stats.of(columnData);
        long distinctCount = columnData.stream().distinct().count();
        return StatsElement.builder()
                .columnName(displayColumnName)
                .distinctCount(distinctCount)
                .count((long) columnData.size())
                .max(columnStats.max())
                .mean(columnStats.mean())
                .min(columnStats.min())
                .stddev(columnStats.sampleStandardDeviation())
                .build();
    }

    private Bucket populateBucket(List<Integer> columnData, StatsElement columnStats, String columnName) {
        int maxData = (int)Math.round(columnStats.getMax());
        int minData = (int)Math.round(columnStats.getMin());
        Map<Integer, BucketRange> bucketRanges = getBucketRangeMap(minData,maxData);
        setBucketCounts(bucketRanges, columnData);
        List<BucketElement> bucketData = convertBucketRangeToBucketElement(bucketRanges);
        return Bucket.builder().bucketData(bucketData).columnName(columnName).build();
    }

    private List<BucketElement> convertBucketRangeToBucketElement(Map<Integer, BucketRange> bucketRanges) {
        List<BucketElement> bucketElements = new ArrayList<>();
        for(int i = 1; i<= Constants.BUCKETCOUNT; ++i) {
            BucketRange bucketRange = bucketRanges.get(i);
            BucketElement bucketElement = BucketElement.builder()
                    .range(bucketRange.getRangeLabel())
                    .count(bucketRange.getCount())
                    .bucketNumber(bucketRange.getBucketNumber())
                    .build();
            bucketElements.add(bucketElement);
        }
        return bucketElements;
    }

    private void setBucketCounts(Map<Integer, BucketRange> bucketRanges, List<Integer> columnData) {
        for(Integer element : columnData) {
            for(int i = 1; i<= Constants.BUCKETCOUNT; ++i){
                BucketRange bucketRange = bucketRanges.get(i);
                if(element >= bucketRange.getBucketRangeStart() && element < bucketRange.getBucketRangeEnd()){
                    bucketRange.setCount(bucketRange.getCount()+1);
                }
            }
        }
    }

    private Map<Integer, BucketRange> getBucketRangeMap(int minData, int maxData) {
        Map<Integer, BucketRange> bucketRanges = new HashMap<>();
        int elementDelta = (maxData - minData+1)/ Constants.BUCKETCOUNT;
        int start = minData;
        int end = 0;
        for(int i = 1; i<= Constants.BUCKETCOUNT; ++i) {
            end = start+elementDelta;
            if(i== Constants.BUCKETCOUNT){
                end = maxData+1;
            }
            BucketRange bucketRange = BucketRange.builder()
                    .bucketNumber(i)
                    .bucketRangeStart(start)
                    .bucketRangeEnd(end)
                    .count(0)
                    .rangeLabel(buildRangeString(start, end))
                    .build();
            bucketRanges.put(i, bucketRange);
            start = bucketRange.getBucketRangeEnd();
        }
        return bucketRanges;
    }

    private String buildRangeString(int buckeRangeStart, int buckeRangeEnd) {
        return String.valueOf(buckeRangeStart).concat(" - ").concat(String.valueOf(buckeRangeEnd));
    }

}
