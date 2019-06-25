package com.jeremie.asm;

/**
 * @author guanhong 2019-06-25.
 */
public class MyClassLoader extends ClassLoader {
    public Class defineClass(String clazzName, byte[] toByteArray) {
        return defineClass(clazzName, toByteArray, 0, toByteArray.length);
    }
}
