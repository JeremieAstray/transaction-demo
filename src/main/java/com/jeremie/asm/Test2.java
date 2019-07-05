package com.jeremie.asm;

import com.jeremie.asm.interceptor.MyMethodInterceptor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author guanhong 2019-06-25.
 */
public class Test2 {

    private final static String CLAZZ_NAME_PATH = "com/jeremie/asm/AsmTest";
    private final static String NEW_CLAZZ_NAME_PATH = "com/jeremie/asm/AsmTestNew";
    private final static String CLAZZ_NAME = "com.jeremie.asm.AsmTest";
    private final static String NEW_CLAZZ_NAME = "com.jeremie.asm.AsmTestNew";

    public static void main(String[] args) throws Throwable {
        AsmTest asmTest = new AsmTest();
        System.out.println(asmTest.getId());
        System.out.println(asmTest.getName());
        System.out.println(asmTest.getAge());
        asmTest.addAge();
        System.out.println(asmTest.getAll());

        //读取类
        //ClassReader classReader = new ClassReader(CLAZZ_NAME);

        //字节码编辑器
        ClassWriter classWriter = new ClassWriter(0);

        classWriter.visit(V1_8, ACC_PUBLIC, NEW_CLAZZ_NAME_PATH, null, CLAZZ_NAME_PATH, null);

        //构造函数
        MethodVisitor mw = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitMethodInsn(INVOKESPECIAL, CLAZZ_NAME_PATH, "<init>", "()V", false);
        mw.visitInsn(RETURN);
        mw.visitMaxs(1, 1);
        mw.visitEnd();

        FieldVisitor fv = classWriter.visitField(ACC_PUBLIC, "methodInterceptor"
                , "Lcom/jeremie/asm/interceptor/MethodInterceptor;", null, null);
        fv.visitEnd();

        //方法
        MethodVisitor mw2 = classWriter.visitMethod(ACC_PUBLIC, "getId", "()I", null, null);
        mw2.visitVarInsn(ALOAD, 0);
        mw2.visitFieldInsn(GETFIELD, NEW_CLAZZ_NAME_PATH, "methodInterceptor", "Lcom/jeremie/asm/interceptor/MethodInterceptor;");
        mw2.visitVarInsn(ALOAD, 0);
        mw2.visitVarInsn(ALOAD, 0);
        mw2.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
        mw2.visitLdcInsn("getId");
        mw2.visitInsn(ICONST_0);
        mw2.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Class");
        mw2.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
        mw2.visitInsn(ICONST_0);
        mw2.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Class");
        mw2.visitMethodInsn(INVOKEINTERFACE, "com/jeremie/asm/interceptor/MethodInterceptor", "intercept", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", true);
        mw2.visitTypeInsn(CHECKCAST, "java/lang/Integer");
        mw2.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
        mw2.visitInsn(IRETURN);
        mw2.visitMaxs(5, 1);
        mw2.visitEnd();

        //mw2.visitMethodInsn(INVOKESPECIAL, CLAZZ_NAME_PATH, "getId", "()V", false);


        //mw2.visitInsn(RETURN);
        //mw2.visitMaxs(1, 1);
        //mw2.visitEnd();

        //字节码访问器
        //ClassVisitor change = new ChangeVisitor(classWriter);

        //
        //ClassReader classReader = new ClassReader(CLAZZ_NAME);
        //classReader.accept(change, EXPAND_FRAMES);
        /*for (Method method : Class.forName(CLAZZ_NAME).getMethods()) {
            MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, method.getName(), "()V", null, null);

        }*/


        //访问者模式，加载ClassVisitor
        //classReader.accept(classWriter, ClassReader.EXPAND_FRAMES);


        //用新的字节码生成类
        File newClazz = new File(ClassLoader.getSystemResource("").getPath() + "com/jeremie/asm", "AsmTestNew.class");
        FileOutputStream fileOutputStream = new FileOutputStream(newClazz);
        fileOutputStream.write(classWriter.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();

        Class clazz = new MyClassLoader().defineClass(NEW_CLAZZ_NAME, classWriter.toByteArray());
        //实例化类
        Object personObj = clazz.getConstructor().newInstance();

        clazz.getDeclaredField("methodInterceptor").set(personObj, new MyMethodInterceptor());

        System.out.println("init Success!");
        System.out.println();

        //反射调用生成类的getId方法
        Method nameMethod = clazz.getDeclaredMethod("getId", null);
        nameMethod.invoke(personObj, null);
        System.out.println();
/*
        //反射调用生成类的getAge方法
        Method getMethod = clazz.getDeclaredMethod("getAge", null);
        Object result = getMethod.invoke(personObj, null);
        System.out.println(result);*/
    }

    /*static class ChangeVisitor extends ClassVisitor {
        private ClassVisitor classVisitor;

        ChangeVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM5, classVisitor);
            this.classVisitor = classVisitor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("<init>")) {
                return methodVisitor;
            }

            if (name.equals("getId")) {
                MethodVisitor newMethodVisitor = classVisitor.visitMethod(access, name, desc, signature, exceptions);
                newMethodVisitor.visitVarInsn(ALOAD, 0);

            }


            return methodVisitor;
        }
    }*/
}
