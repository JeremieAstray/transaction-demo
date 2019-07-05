package com.jeremie.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * @author guanhong 2019-06-25.
 */
public class Test {

    private final static String CLAZZ_NAME = "com.jeremie.asm.AsmTest";

    public static void main(String[] args) throws Throwable {
        AsmTest asmTest = new AsmTest();
        System.out.println(asmTest.getId());
        System.out.println(asmTest.getName());
        System.out.println(asmTest.getAge());
        asmTest.addAge();
        System.out.println(asmTest.getAll());

        //读取类
        ClassReader classReader = new ClassReader(CLAZZ_NAME);

        //字节码编辑器
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        //字节码访问器
        ClassVisitor change = new ChangeVisitor(classWriter);

        //访问者模式，加载ClassVisitor
        classReader.accept(change, ClassReader.EXPAND_FRAMES);

        //用新的字节码生成类
        Class clazz = new MyClassLoader().defineClass(CLAZZ_NAME, classWriter.toByteArray());
        File newClazz = new File(ClassLoader.getSystemResource("").getPath() + "com/jeremie/asm", "AsmTestNew.class");
        FileOutputStream fileOutputStream = new FileOutputStream(newClazz);
        fileOutputStream.write(classWriter.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();
        //实例化类
        Object personObj = clazz.newInstance();
        System.out.println("init Success!");
        System.out.println();

        //反射调用生成类的addAge方法
        Method nameMethod = clazz.getDeclaredMethod("addAge", null);
        nameMethod.invoke(personObj, null);
        System.out.println();

        //反射调用生成类的getAge方法
        Method getMethod = clazz.getDeclaredMethod("getAge", null);
        Object result = getMethod.invoke(personObj, null);
        System.out.println(result);

    }

    static class ChangeVisitor extends ClassVisitor {

        ChangeVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM5, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("<init>")) {
                return methodVisitor;
            }
            return new ChangeAdapter(Opcodes.ASM4, methodVisitor, access, name, desc);
        }
    }

    static class ChangeAdapter extends AdviceAdapter {
        //private int startTimeId = -1;
        private String methodName = null;

        ChangeAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            methodName = name;
            //startTimeId = newLocal(Type.LONG_TYPE);
            //mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            //mv.visitIntInsn(LSTORE, startTimeId);
        }

        @Override
        protected void onMethodEnter() {
            //方法开始前往字节码中插入一段输出方法开始的代码
            addMethodFlag("start");
        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);
            if (methodName.equals("addAge")) {
                //age字段+1
                mv.visitVarInsn(ALOAD, 0);
                mv.visitInsn(DUP);
                mv.visitFieldInsn(GETFIELD, "com/jeremie/asm/AsmTest", "age", Type.getDescriptor(int.class));
                mv.visitInsn(ICONST_1);
                mv.visitInsn(IADD);
                mv.visitFieldInsn(PUTFIELD, "com/jeremie/asm/AsmTest", "age", Type.getDescriptor(int.class));

                //方法调用
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/jeremie/asm/AsmTest", "getAge", "()I", false);
                mv.visitInsn(POP);

                //方法调用(带参数)
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(BIPUSH, 100);
                mv.visitVarInsn(SIPUSH, 200);
                mv.visitVarInsn(SIPUSH, 300);
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/jeremie/asm/AsmTest", "testParam", "(III)Ljava/lang/String;", false);
                mv.visitVarInsn(ASTORE, 1);
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitVarInsn(ALOAD, 1);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            }

            //方法结束前往字节码中插入一段输出方法结束的代码
            addMethodFlag("end");

        }

        private void addMethodFlag(String flag) {
            int durationId = newLocal(Type.LONG_TYPE);
            /*mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startTimeId);
            mv.visitInsn(LSUB);
            mv.visitVarInsn(LSTORE, durationId);*/
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("The method " + methodName + " is " + flag);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            //mv.visitVarInsn(LLOAD, startTimeId);
            //mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }
}
