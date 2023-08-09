package com.lukaszsuma.regexdatagenerator.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum CountryLettersToNumber {
    A("10"),
    B("11"),
    C("12"),
    D("13"),
    E("14"),
    F("15"),
    G("16"),
    H("17"),
    I("18"),
    J("19"),
    K("20"),
    L("21"),
    M("22"),
    N("23"),
    O("24"),
    P("25"),
    Q("26"),
    R("27"),
    S("28"),
    T("29"),
    U("30"),
    V("31"),
    W("32"),
    X("33"),
    Y("34"),
    Z("35");

    private static final Logger logger = LogManager.getLogger(CountryLettersToNumber.class);
    private final String number;

    CountryLettersToNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public static String convertCountryLettersToNumber(String letters) {
        logger.debug("convertCountryLettersToNumber");
        String[] arr = letters.toUpperCase().split(StringSeparator.EMPTY_STRING);
        String first = CountryLettersToNumber.valueOf(arr[0]).getNumber();
        String second = CountryLettersToNumber.valueOf(arr[1]).getNumber();
        return first + second;
    }
}
