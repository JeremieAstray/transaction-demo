package com.jeremie;

/**
 * @author guanhong 2019-06-25.
 */
public class Main {
    public int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(main.add(5, 100));
    }
}
