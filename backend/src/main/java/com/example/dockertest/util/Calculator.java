package com.example.dockertest.util;

public class Calculator {
    private static double calculateBMR(double weight, double height, int age, String sex) {
        if (sex.equalsIgnoreCase("male")) {
            return 66.4730 + (5.0033 * height) + (13.7516 * weight) - (6.7550 * age);
        } else {
            return 655.0955 + (1.8496 * height) + (9.5634 * weight) - (4.6756 * age);
        }
    }

    public static double calculateCorrectedMET(double averageMET, double weight, double height, int age, String sex) {
        double bmr = calculateBMR(weight, height, age, sex);
        double bmrMlKgMin = bmr / 1440 / 5 / weight * 1000;
        return averageMET * 3.5 / bmrMlKgMin;
    }
}
