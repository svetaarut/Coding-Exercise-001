package com.transunion.test.codingexercise.controller;

import com.transunion.test.codingexercise.model.Bucket;
import com.transunion.test.codingexercise.model.SoundexResponse;
import com.transunion.test.codingexercise.model.Statistics;
import com.transunion.test.codingexercise.service.ConsumerService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.ValidationException;
import javax.ws.rs.QueryParam;


@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(value = "/app/v1")
public class ConsumerController {

    @Autowired
    private ConsumerService service;

    @ApiOperation(value = "An endpoint which returns the statistics for all the numeric columns from the csv file. The statistics include min, max, count, distinct count, mean and standard deviation.")
    @GetMapping("/statistics")
    public ResponseEntity getStatistics() {
        Statistics response = service.getStatistics();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "This endpoint returns the bucketized data only for numeric columns. The column name is passed as a query parameter in the URL.")
    @GetMapping("/bucketdata")
    public ResponseEntity getBucketDataByColumnName(@QueryParam("columnName") String columnName) {
        try {
            Bucket response = service.getBucketDataByColumnName(columnName);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "An endpoint which implements the Soundex phonetic algorithm. Given a column name and a value to match, this will return all the values which fall within 100 range for Soundex codes.")
    @GetMapping("/match")
    public ResponseEntity getMatchByColumnName(@QueryParam ("columnName") String columnName, @QueryParam("name") String name) {
        try {
            SoundexResponse response = service.getMatchByColumnName(columnName, name);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(ValidationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e1.getMessage());
        }
    }

}
