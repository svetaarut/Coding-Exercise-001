package com.transunion.test.codingexersise;

import com.transunion.test.codingexercise.config.AppConfig;
import com.transunion.test.codingexercise.constants.Constants;
import com.transunion.test.codingexercise.model.Bucket;
import com.transunion.test.codingexercise.model.SoundexResponse;
import com.transunion.test.codingexercise.model.Statistics;
import com.transunion.test.codingexercise.service.ConsumerService;
import com.transunion.test.codingexercise.service.ConsumerServiceImpl;
import com.transunion.test.codingexercise.service.DataProviderService;
import com.transunion.test.codingexercise.service.DataProviderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ConsumerServiceImpl.class,
        DataProviderServiceImpl.class,
        AppConfig.class
})
public class ConsumerServiceTest {
    @Autowired
    private ConsumerService service;

    @Autowired
    private DataProviderService dataProvidedService;

    @Autowired
    AppConfig appConfig;

    @Test
    public void given_validRequest_when_getStatistics_then_returnStatistics(){
        Statistics result = service.getStatistics();
        assertEquals(3,result.getStatistics().getNumericDistributions().size());
    }

    @Test
    public void given_validRequest_when_getBucketDataByColumnName_then_returnAgeBucket(){
        Bucket result = service.getBucketDataByColumnName(Constants.AGE);
        assertEquals(5,result.getBucketData().size());
    }

    @Test
    public void given_invalidRequest_when_getBucketDataByColumnName_then_throwsException(){
        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
        {
            service.getBucketDataByColumnName(Constants.FIRSTNAME);
        });
        String expectedMessage = "Bad Request: Only Numeric Columns are allowed: (age, vantageScore, ficoScore).";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void given_validRequest_when_getMatchByColumnName_then_returnAgeBucket(){
        SoundexResponse result = service.getMatchByColumnName(Constants.FIRSTNAME, "John");
        assertTrue(result.getOutput().size() > 0);
    }

    @Test
    public void given_invalidRequest_when_getMatchByColumnName_then_throwsException(){
        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
        {
            service.getMatchByColumnName(Constants.AGE, "John");
        });
        String expectedMessage = "Bad Request: Only String Columns are allowed: (firstName, lastName, email)";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void given_validRequest_when_getMatchByColumnName_andNoMatches_then_throwsException(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () ->
        {
            service.getMatchByColumnName(Constants.LASTNAME, "X23456789");
        });
        String expectedMessage = "No matches found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
