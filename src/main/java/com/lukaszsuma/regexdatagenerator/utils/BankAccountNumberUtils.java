package com.lukaszsuma.regexdatagenerator.utils;

import java.util.Random;

public class BankAccountNumberUtils {

    private static final Random RANDOM = new Random();
    private static final StringBuilder SB = new StringBuilder();

    public static String generateBankAccountNumber(String country, String bankName, boolean formatted,
                                                   boolean withLetters) {
        int index = RANDOM.nextInt(0, PolandBankId.values().length);
        String bankId = PolandBankId.values()[index].getId();
        if (bankName != null && !bankName.isBlank()) {
            try {
                PolandBankId value = PolandBankId.valueOf(bankName.toUpperCase());
                bankId = value.getId();

            } catch (IllegalArgumentException ignore) {
            }
        }

        SB.append(bankId);
        for (int i = 0; i < 3; i++) {
            SB.append(RANDOM.nextInt(0, 10));
        }
        SB.append(generateControlNumber(SB.toString().split("")));
        for (int i = 0; i < 16; i++) {
            SB.append(RANDOM.nextInt(0, 10));
        }

        String defaultCountryLettersValue = CountryLettersToNumber.P.getNumber() + CountryLettersToNumber.L.getNumber();
        if (isCountryValid(country)) {
            try {
                defaultCountryLettersValue = convertCountryLettersToNumber(country);
            } catch (IllegalArgumentException ignore) {
            }
        }

        String currVal = SB.toString().concat(defaultCountryLettersValue).concat("00");
        String substring = currVal.substring(0, currVal.length() / 2);
        String substring1 = currVal.substring(currVal.length() / 2);
        long firstPart = Long.parseLong(substring);
        long mod = firstPart % 97;
        String restOfNumber = mod + substring1;
        long rest = Long.parseLong(restOfNumber);
        long num = rest % 97;
        int IBANControlNumber = 98 - (int) num;
        if (IBANControlNumber < 10) {
            SB.insert(0, IBANControlNumber).insert(0, 0);
        } else {
            SB.insert(0, IBANControlNumber);
        }
        String result = SB.toString();
        SB.delete(0, SB.length());
        return result;
    }

    private static boolean isCountryValid(String countryLetters) {
        boolean isValid = false;
        if (countryLetters != null && !countryLetters.isBlank() && countryLetters.length() == 2) {
            try {
                AvailableCountries.valueOf(countryLetters.toUpperCase());
                isValid = true;
            } catch (IllegalArgumentException ignore) {
            }
        }
        return isValid;
    }

    private static String convertCountryLettersToNumber(String letters) {
        String[] arr = letters.toUpperCase().split(StringSeparator.EMPTY_STRING);
        String first = CountryLettersToNumber.valueOf(arr[0]).getNumber();
        String second = CountryLettersToNumber.valueOf(arr[1]).getNumber();
        return first + second;
    }

    private static int generateControlNumber(String[] arrOfNums) {
        int sumControlNumber = Integer.parseInt(arrOfNums[0]) * 7 +
                Integer.parseInt(arrOfNums[1]) +
                Integer.parseInt(arrOfNums[2]) * 3 +
                Integer.parseInt(arrOfNums[3]) * 9 +
                Integer.parseInt(arrOfNums[4]) * 7 +
                Integer.parseInt(arrOfNums[5]) * 11 +
                Integer.parseInt(arrOfNums[6]) * 3;
        return sumControlNumber % 10;
    }
}
