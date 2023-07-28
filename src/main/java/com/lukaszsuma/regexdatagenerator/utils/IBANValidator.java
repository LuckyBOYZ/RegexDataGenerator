package com.lukaszsuma.regexdatagenerator.utils;

public class IBANValidator {

    public static int getIbanControlNumber(StringBuilder sb, String defaultCountryLettersValue, boolean isFormatted,
                                           boolean isWithLetters) {
        String ibanString = sb.toString();
        if(isFormatted) {
            ibanString = ibanString.replace(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_STRING);
        }
        if(isWithLetters) {
            ibanString = ibanString.substring(4);
        }
        String currVal = ibanString.concat(defaultCountryLettersValue).concat("00");
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
