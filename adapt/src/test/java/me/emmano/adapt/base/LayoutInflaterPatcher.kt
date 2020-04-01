package me.emmano.adapt.base

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ACC_PRIVATE
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.Opcodes.ACC_SUPER
import org.objectweb.asm.Opcodes.ACONST_NULL
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Opcodes.DUP
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.NEW
import org.objectweb.asm.Opcodes.PUTSTATIC
import org.objectweb.asm.Opcodes.RETURN
import org.objectweb.asm.Type

class LayoutInflaterPatcher {
    companion object {
        fun patch(): ByteArray {
            val cw = ClassWriter(0)
            var mv: MethodVisitor

            cw.visit(52, ACC_PUBLIC + ACC_SUPER, "android/view/LayoutInflater", null, "java/lang/Object", null);

                val fv = cw.visitField(ACC_PRIVATE + ACC_STATIC, "inflater", "Landroid/view/LayoutInflater;", null, null);
                fv.visitEnd();

                mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
                mv.visitInsn(RETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();

                mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "from", "(Landroid/content/Context;)Landroid/view/LayoutInflater;", null, null);
                mv.visitCode();
                mv.visitFieldInsn(GETSTATIC, "android/view/LayoutInflater", "inflater", "Landroid/view/LayoutInflater;");
                mv.visitInsn(ARETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();

                mv = cw.visitMethod(ACC_PUBLIC, "inflate", "(ILandroid/view/ViewGroup;Z)Landroid/view/View;", null, null);
                mv.visitCode();
                mv.visitLdcInsn(Type.getType("Landroid/view/View;"));
                mv.visitMethodInsn(INVOKESTATIC, "org/mockito/Mockito", "mock", "(Ljava/lang/Class;)Ljava/lang/Object;", false);
                mv.visitTypeInsn(CHECKCAST, "android/view/View");
                mv.visitInsn(ARETURN);
                mv.visitMaxs(1, 4);
                mv.visitEnd();

                mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
                mv.visitCode();
                mv.visitLdcInsn(Type.getType("Landroid/view/LayoutInflater;"));
                mv.visitMethodInsn(INVOKESTATIC, "org/mockito/Mockito", "mock", "(Ljava/lang/Class;)Ljava/lang/Object;", false);
                mv.visitTypeInsn(CHECKCAST, "android/view/LayoutInflater");
                mv.visitFieldInsn(PUTSTATIC, "android/view/LayoutInflater", "inflater", "Landroid/view/LayoutInflater;");
                mv.visitInsn(RETURN);
                mv.visitMaxs(1, 0);
                mv.visitEnd();

            cw.visitEnd();



            return cw.toByteArray()
        }
    }
}