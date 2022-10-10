package org.example;

import static com.google.common.base.Objects.equal;

public class HelloOtus {
    public static void compare(String a, String b){
        if (equal(a, b)) {
            System.out.printf("Variables are equal");
        }
        else {
            System.out.printf("Variables are NOT equal");
        }
    }
}
