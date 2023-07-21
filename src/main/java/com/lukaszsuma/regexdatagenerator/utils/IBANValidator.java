package com.lukaszsuma.regexdatagenerator.utils;

public class IBANValidator {

    public static int getIbanControlNumber(StringBuilder sb, String defaultCountryLettersValue) {
        String currVal = sb.toString().concat(defaultCountryLettersValue).concat("00");
        String substring = currVal.substring(0, currVal.length() / 2);
        String substring1 = currVal.substring(currVal.length() / 2);
        long firstPart = Long.parseLong(substring);
        long mod = firstPart % 97;
        String restOfNumber = mod + substring1;
        long rest = Long.parseLong(restOfNumber);
        long num = rest % 97;
        return 98 - (int) num;
    }
}
