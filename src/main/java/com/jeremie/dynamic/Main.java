package com.jeremie.dynamic;

/**
 * @author guanhong 2019-06-13.
 */
public class Main {
    public static void main(String[] args) {
/*

        String str = new StringBuffer().append("te").append("st").toString();
        System.out.println(str == str.intern()); //true

        String str1 = new StringBuffer().append("ja").append("va").toString();
        System.out.println(str1 == str1.intern()); //true
*/
            String[] test = new String[]{"123","455","6456"};
            StringBuilder s = new StringBuilder();
            for (String test2 :test) {
                s.append(test2);
                s.append("，");
            }
            System.out.println(s.toString().substring(0,s.length()-1));
    }
}
