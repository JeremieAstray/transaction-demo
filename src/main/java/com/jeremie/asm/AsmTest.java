package com.jeremie.asm;

/**
 * @author guanhong 2019-06-25.
 */
public class AsmTest {

    private String name = "jeremie";
    private int age = 25;
    private int id = 2206710;

    public String getName() {
        return name;
    }

    public int getAge() {
        System.out.println("method getAge");
        return age;
    }

    public int getId() throws Throwable {
        return id;
    }

    public String getAll() {
        return name + "#" + age + "#" + id;
    }

    public void addAge() throws Throwable {
        System.out.println("method addAge");
        age++;
        String temp = testParam(100, 200, 300);
        //System.out.println(temp);
    }

    public String testParam(int test, int test2, int test3) throws Throwable {
        System.out.println(test + " " + test2 + " " + test3 + " testParam");
        return test + " testParam";
    }
}
