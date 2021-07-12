package com.example.demo.utils;

import java.util.Random;

public class RandomHelper {
    public static String getAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz";
  
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
        return sb.toString();
    }

    public static int getNumberic() {
        return (int) Math.random();
    }

    public static long getLong() {
        return (long) Math.random();
    }

    public static int getNumberic(int from, int to) {
        Random rng = new Random();
        int min = from;
        int max = to;
        int upperBound = max - min + 1; // upper bound is exclusive, so +1
        int num = min + rng.nextInt(upperBound);

        return num;
    }
}
