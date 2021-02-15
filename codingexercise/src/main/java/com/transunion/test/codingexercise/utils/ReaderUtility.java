package com.transunion.test.codingexercise.utils;

import com.transunion.test.codingexercise.model.Consumer;
import org.apache.commons.codec.language.Soundex;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReaderUtility {
    public static List<Consumer> getConsumerData(String fileName) throws IOException {
        List<Consumer> consumers = new ArrayList<>();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            if(is != null) {
                reader = new BufferedReader(new InputStreamReader(is));
                reader.readLine(); //skip header
                String line = reader.readLine();
                Soundex soundex = new Soundex();
                while ((line != null) && !line.isEmpty()) {
                    consumers.add(buildConsumerFromLine(line, soundex));
                    line = reader.readLine();
                }
            }
        }finally{
            if(is != null) {
                is.close();
            }
            if(reader != null) {
                reader.close();
            }
        }
        return consumers;
    }

    private static Consumer buildConsumerFromLine(String line, Soundex soundex) {
        String[] consumerInfo = line.split(",");
        return Consumer.builder()
                .firstName(consumerInfo[0])
                .lastName(consumerInfo[1])
                .email(consumerInfo[2])
                .age(Integer.parseInt(consumerInfo[3]))
                .vantageScore(Integer.parseInt(consumerInfo[4]))
                .ficoScore(Integer.parseInt(consumerInfo[5]))
                .soundexFirstNameCode(soundex.encode(consumerInfo[0]))
                .soundexLastNameCode(soundex.encode(consumerInfo[1]))
                .soundexEmailCode(soundex.encode(consumerInfo[2]))
                .build();
    }

}
