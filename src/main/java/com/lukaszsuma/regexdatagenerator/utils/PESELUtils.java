package com.lukaszsuma.regexdatagenerator.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PESELUtils {

    private static final Logger logger = LogManager.getLogger(PESELUtils.class);
    private static final Random RANDOM = new Random();
    private static final Map<Integer, Integer> DAYS_PER_MONTH = new HashMap<>(12, 1f);
    private static final int[] FEMALE_NUMS = {0, 2, 4, 6, 8};
    private static final int[] MALE_NUMS = {1, 3, 5, 7, 9};

    static {
        DAYS_PER_MONTH.put(1, 31);
        DAYS_PER_MONTH.put(3, 31);
        DAYS_PER_MONTH.put(4, 30);
        DAYS_PER_MONTH.put(5, 31);
        DAYS_PER_MONTH.put(6, 30);
        DAYS_PER_MONTH.put(7, 31);
        DAYS_PER_MONTH.put(8, 31);
        DAYS_PER_MONTH.put(9, 30);
        DAYS_PER_MONTH.put(10, 31);
        DAYS_PER_MONTH.put(11, 30);
        DAYS_PER_MONTH.put(12, 31);
    }

    public static String generateRandomPESEL(boolean canBeAsBornAfter2000, boolean isFemale, boolean onlyAdults) {
        logger.debug("generateRandomPESEL");
        logger.debug("Method parameters: canBeAsBornAfter2000={}, isFemale={}, onlyAdults={}", canBeAsBornAfter2000,
                isFemale, onlyAdults);
        StringBuilder sb = new StringBuilder();

        int yearNum = generateYearByConditions(canBeAsBornAfter2000, onlyAdults);
        addComputedValue(yearNum, sb);

        int monthNum = RANDOM.nextInt(canBeAsBornAfter2000 && onlyAdults ?
                LocalDateTime.now().getMonthValue() : 1, 13);
        int dayRange;
        if (monthNum == 2) {
            int isLeapYear = yearNum % 4;
            if (isLeapYear == 0) {
                dayRange = 28;
            } else {
                dayRange = 29;
            }
        } else {
            dayRange = DAYS_PER_MONTH.get(monthNum);
        }
        if (canBeAsBornAfter2000) {
            monthNum += 20;
        }
        addComputedValue(monthNum, sb);

        int currentDaysOfMonth = LocalDateTime.now().getDayOfMonth();
        int outerBoundOfDays = dayRange + 1;
        int dayNum;
        if (currentDaysOfMonth == outerBoundOfDays) {
            dayNum = currentDaysOfMonth;
        } else {
            dayNum = RANDOM.nextInt(canBeAsBornAfter2000 && onlyAdults ?
                    currentDaysOfMonth : 1, outerBoundOfDays);
        }
        addComputedValue(dayNum, sb);

        int serialNumberWithoutSex = RANDOM.nextInt(1000);
        if (serialNumberWithoutSex < 10) {
            sb.append("00").append(serialNumberWithoutSex);
        } else if (serialNumberWithoutSex < 100) {
            sb.append("0").append(serialNumberWithoutSex);
        } else {
            sb.append(serialNumberWithoutSex);
        }

        int index = RANDOM.nextInt(5);
        if (isFemale) {
            sb.append(FEMALE_NUMS[index]);
        } else {
            sb.append(MALE_NUMS[index]);
        }

        String[] arrOfNums = sb.toString().split(StringSeparator.EMPTY_STRING);
        int controlNumber = computeControlNumber(arrOfNums);
        sb.append(controlNumber);
        logger.debug("Generated PESEL: {}", sb);
        return sb.toString();
    }

    private static int generateYearByConditions(boolean canBeAsBornAfter2000, boolean onlyAdults) {
        logger.debug("generateYearByConditions");
        int twoLastDigitsOfCurrentYear = getLastDigitsOfCurrentYear();
        int endIndex = canBeAsBornAfter2000 ? onlyAdults ? twoLastDigitsOfCurrentYear - 17 : twoLastDigitsOfCurrentYear : 100;
        return RANDOM.nextInt(0, endIndex);
    }

    private static int getLastDigitsOfCurrentYear() {
        logger.debug("getLastDigitsOfCurrentYear");
        int currentYear = LocalDateTime.now().getYear();
        return Integer.parseInt(String.valueOf(currentYear).substring(2));
    }

    private static void addComputedValue(int num, StringBuilder sb) {
        logger.debug("addComputedValue");
        if (num < 10) {
            sb.append("0").append(num);
        } else {
            sb.append(num);
        }
    }

    private static int computeControlNumber(String[] arrOfNums) {
        logger.debug("computeControlNumber");
        String sumControlNumber = String.valueOf(Integer.parseInt(arrOfNums[0]) +
                Integer.parseInt(arrOfNums[1]) * 3 +
                Integer.parseInt(arrOfNums[2]) * 7 +
                Integer.parseInt(arrOfNums[3]) * 9 +
                Integer.parseInt(arrOfNums[4]) +
                Integer.parseInt(arrOfNums[5]) * 3 +
                Integer.parseInt(arrOfNums[6]) * 7 +
                Integer.parseInt(arrOfNums[7]) * 9 +
                Integer.parseInt(arrOfNums[8]) +
                Integer.parseInt(arrOfNums[9]) * 3);
        int lastNumOfSumControlNum = Integer.parseInt(sumControlNumber.substring(sumControlNumber.length() - 1));
        return lastNumOfSumControlNum == 0 ? 0 : 10 - lastNumOfSumControlNum;
    }
}
