package com.example.dockertest.util;

public class BMRCalculator {
    public static double calculateBMR(double weight, int height, int age, String gender) {
        if(gender.equalsIgnoreCase("male")) {
            return (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            return (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
    }

    public static double calculateBMRHarrisBenedict(double weight, int height, int age, String gender) {
        if(gender.equalsIgnoreCase("male")) {
            return (13.397 * weight) + (4.799 * height) - (5.677 * age) + 88.362;
        } else {
            return (9.247 * weight) + (3.098 * height) - (4.330 * age) + 447.593;
        }
    }
}
