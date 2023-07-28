package com.lukaszsuma.regexdatagenerator.utils;

import java.util.Random;

public class BankAccountNumberUtils {

    private static final Random RANDOM = new Random();
    private static final StringBuilder SB = new StringBuilder();

    public static String generateBankAccountNumber(String country, String bankName, boolean formatted,
                                                   boolean withLetters, int defaultIndexForBankName) {
        String bankId = PolandBankId.values()[defaultIndexForBankName].getId();
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

        String countryLetters = CountryLettersToNumber.P.name() + CountryLettersToNumber.L.name();
        String defaultCountryLettersValue = CountryLettersToNumber.P.getNumber() + CountryLettersToNumber.L.getNumber();
        if (isCountryValid(country)) {
            countryLetters = country;
            defaultCountryLettersValue = CountryLettersToNumber.convertCountryLettersToNumber(country);
        }
        int IBANControlNumber = IBANValidator.getIbanControlNumber(SB, defaultCountryLettersValue, false, false);
        if (IBANControlNumber < 10) {
            SB.insert(0, IBANControlNumber).insert(0, 0);
        } else {
            SB.insert(0, IBANControlNumber);
        }

        if (formatted) {
            if (withLetters) {
                SB.insert(0, countryLetters);
                for (int i = 0; i < SB.length() / 4; i++) {
                    int offset = 4 * i + i;
                    if (offset < SB.length()) {
                        SB.insert(offset, StringSeparator.EMPTY_SPACE);
                    }
                }
            } else {
                for (int i = 0; i < SB.length() / 4; i++) {
                    int offset = 4 * i + 2 + i;
                    if (offset < SB.length()) {
                        SB.insert(offset, StringSeparator.EMPTY_SPACE);
                    }
                }
            }
        } else if (withLetters) {
            SB.insert(0, countryLetters);
        }

        String result = SB.toString().trim();
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
