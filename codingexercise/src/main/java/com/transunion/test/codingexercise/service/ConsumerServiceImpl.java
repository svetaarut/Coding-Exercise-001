package com.transunion.test.codingexercise.service;

import com.transunion.test.codingexercise.constants.Constants;
import com.transunion.test.codingexercise.model.Bucket;
import com.transunion.test.codingexercise.model.ConsumerSoundexData;
import com.transunion.test.codingexercise.model.SoundexResponse;
import com.transunion.test.codingexercise.model.Statistics;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.language.Soundex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerServiceImpl implements ConsumerService{

    @Autowired
    private DataProviderService dataProviderService;

    @Override
    public Statistics getStatistics() {
        return dataProviderService.getStatistics();
    }

    @Override
    public Bucket getBucketDataByColumnName(String columnName) {
        if(!isValidColumn(columnName, Constants.VALIDNUMERICCOLUMNS)){
            throw new ValidationException("Bad Request: Only Numeric Columns are allowed: (age, vantageScore, ficoScore).");
        }
        return dataProviderService.getBucketDataByColumnName(columnName);
    }

    @Override
    public SoundexResponse getMatchByColumnName(String columnName, String name) {
        if(!isValidColumn(columnName, Constants.VALIDSTRINGCOLUMNS)){
            throw new ValidationException("Bad Request: Only String Columns are allowed: (firstName, lastName, email)");
        }
        List <String> matches = getSoundexMatches(dataProviderService.getSoundexMapByColumnName(columnName.toUpperCase()), name);
        if(CollectionUtils.isEmpty(matches)){
            throw new RuntimeException("No matches found.");
        }
        return SoundexResponse.builder().output(matches).build();
    }

    private boolean isValidColumn(String columnName, List<String> validstringcolumns) {
        return validstringcolumns.contains(columnName.toUpperCase());
    }

    private List<String> getSoundexMatches(Map<String, List<ConsumerSoundexData>> soundexCodeMap, String name) {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.encode(name);
        String soundexKey = soundexCode.substring(0,1);
        List<ConsumerSoundexData> consumerSoundexDataList = soundexCodeMap.get(soundexKey);

        if(CollectionUtils.isEmpty(consumerSoundexDataList)) {
            return null;
        }
        final Integer soundexIntCode = getSoundexIntCode(soundexCode);
        final Integer beginRange = soundexIntCode - Constants.CODEXRANGE;
        final Integer endRange = soundexIntCode + Constants.CODEXRANGE;

        return consumerSoundexDataList.stream().filter(
                e->e.getSoundexIntNameCode() >= beginRange && e.getSoundexIntNameCode() <= endRange)
                .map(e->e.getName()).distinct().collect(Collectors.toList());
    }

    private Integer getSoundexIntCode(String soundexCode) {
       return Integer.valueOf(soundexCode.substring(1));
    }

}
