package com.jeremie.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Method;

/**
 * @author guanhong 2019-06-25.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        AsmTest asmTest = new AsmTest();
        System.out.println(asmTest.getId());
        System.out.println(asmTest.getName());
        System.out.println(asmTest.getAge());
        asmTest.addAge();
        System.out.println(asmTest.getAll());


        String clazzName = "com.jeremie.asm.AsmTest";

        final int flag = ClassWriter.COMPUTE_MAXS;
        ClassReader classReader = new ClassReader(clazzName);
        ClassWriter classWriter = new ClassWriter(flag);
        ClassVisitor change = new ChangeVisitor(classWriter);
        classReader.accept(change, ClassReader.EXPAND_FRAMES);

        Class clazz = new MyClassLoader().defineClass(clazzName, classWriter.toByteArray());
        Object personObj = clazz.newInstance();
        System.out.println("init Success!");
        System.out.println();
        Method nameMethod = clazz.getDeclaredMethod("addAge", null);
        nameMethod.invoke(personObj, null);
        System.out.println();

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
        private int startTimeId = -1;
        private String methodName = null;

        ChangeAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            methodName = name;
            //startTimeId = newLocal(Type.LONG_TYPE);
            //mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            //mv.visitIntInsn(LSTORE, startTimeId);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("The method " + methodName + " is start ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            //mv.visitVarInsn(LLOAD, startTimeId);
            //mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);
            /*if (methodName.equals("addAge")) {
                //mv.visitInsn(ALOAD);
                //mv.visitInsn(DUP);
                mv.visitFieldInsn(GETFIELD, "V", "age", Type.getDescriptor(int.class));
                mv.visitInsn(IADD);
                mv.visitFieldInsn(PUTFIELD, "V", "age", Type.getDescriptor(int.class));
            }*/
            int durationId = newLocal(Type.LONG_TYPE);
            /*mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startTimeId);
            mv.visitInsn(LSUB);
            mv.visitVarInsn(LSTORE, durationId);*/
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("The method " + methodName + " is end ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            //mv.visitVarInsn(LLOAD, startTimeId);
            //mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }
}