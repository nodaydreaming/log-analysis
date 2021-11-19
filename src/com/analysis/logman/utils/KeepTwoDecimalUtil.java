package com.analysis.logman.utils;

/**
 * 小数保留两位数据
 */
public class KeepTwoDecimalUtil {
    public double keepTwoDecimalPlaces(double num) {
        String line = String.format("%.2f", num);
        double d = Double.valueOf(line);
        return d;
    }

//    public static void main(String[] args) {
//        KeepTwoDecimalUtil keepTwoDecimalUtil = new KeepTwoDecimalUtil();
//        System.out.println(keepTwoDecimalUtil.keepTwoDecimalPlaces(123.241431));
//    }
}
