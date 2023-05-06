package com.lukaszsuma.regexdatagenerator.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PESELUtils {

    private static final Random RANDOM = new Random();
    private static final Map<Integer, Integer> DAYS_PER_MONTH = new HashMap<>(11);
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
        int currentYearNum = Integer.parseInt(String.valueOf(LocalDateTime.now().getYear() - (onlyAdults ? 18 : 0)).substring(2));
        StringBuilder sb = new StringBuilder();

        int yearNum = RANDOM.nextInt(100);
        addComputeValue(yearNum, sb);

        int monthNum = RANDOM.nextInt(1, 13);
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
        if (yearNum <= currentYearNum && canBeAsBornAfter2000) {
            monthNum += 20;
        }
        addComputeValue(monthNum, sb);

        int dayNum = RANDOM.nextInt(1, dayRange + 1);
        addComputeValue(dayNum, sb);

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

        String[] arrOfNums = sb.toString().split("");
        int controlNumber = computeControlNumber(arrOfNums);
        sb.append(controlNumber);
        return sb.toString();
    }

    private static void addComputeValue(int num, StringBuilder sb) {
        if (num < 10) {
            sb.append("0").append(num);
        } else {
            sb.append(num);
        }
    }

    private static int computeControlNumber(String[] arrOfNums) {
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
