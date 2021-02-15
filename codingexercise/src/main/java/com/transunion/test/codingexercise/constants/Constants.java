package com.transunion.test.codingexercise.constants;

import java.util.Arrays;
import java.util.List;

public final class Constants {
    private Constants() {
        // restrict instantiation
    }

    public final static String AGE = "AGE";
    public final static String VANTAGESCORE = "VANTAGESCORE";
    public final static String FICOSCORE = "FICOSCORE";
    public final static String FIRSTNAME = "FIRSTNAME";
    public final static String LASTNAME = "LASTNAME";
    public final static String EMAIL = "EMAIL";
    public final static Integer BUCKETCOUNT = 5;
    public final static Integer CODEXRANGE = 100;
    public final static List<String> VALIDNUMERICCOLUMNS = Arrays.asList(AGE, VANTAGESCORE, FICOSCORE);
    public final static List<String> VALIDSTRINGCOLUMNS = Arrays.asList(FIRSTNAME, LASTNAME, EMAIL);
    public final static String DISPLAYAGE = "age";
    public final static String DISPLAYVANTAGESCORE = "vantageScore";
    public final static String DISPLAYFICOSCORE = "ficoScore";
}