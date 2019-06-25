package com.jeremie;

/**
 * @author guanhong 2019-06-25.
 */
public class InheritFieldTest {
    Father father = new Son();

    static class Father {
        private String name = "father";

        Father() {
            tellName();
        }

        public void tellName() {
            System.out.println("father is your " + name);
        }
    }

    static class Son extends Father {

        private String name = "son";

        public void tellName() {
            System.out.println("son is your " + name);
        }
    }

    public static void main(String[] args) {
        new InheritFieldTest();
    }
}

