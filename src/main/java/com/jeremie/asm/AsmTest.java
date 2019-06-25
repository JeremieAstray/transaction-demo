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

    public int getId() {
        return id;
    }

    public String getAll() {
        return name + "#" + age + "#" + id;
    }

    public void addAge() {
        System.out.println("method addAge");
        age++;
    }
}
