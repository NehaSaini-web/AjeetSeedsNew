package com.example.ajeetseeds.golobalClass;

import java.text.DecimalFormat;

public class StaticMethods {
    public static String removeDecimal(float number) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(number);
    }
    public static String removeDecimalKG(double number) {
        DecimalFormat df = new DecimalFormat("###.###");
        return df.format(number);
    }
    public static String removeDecimal(String number) {
        float decimalvalue = Float.parseFloat(number);
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(decimalvalue);
    }

    public static boolean check_size(String size) {
        try {
            int check = Integer.parseInt(size);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static int Int_size(String size) {
        try {
            return Integer.parseInt(size);
        } catch (Exception e) {
           return  0;
        }
    }
}
