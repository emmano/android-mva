package me.emmano.patch.patching

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ACC_PRIVATE
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.Opcodes.ACC_SUPER
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ARETURN
import org.objectweb.asm.Opcodes.CHECKCAST
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.INVOKESTATIC
import org.objectweb.asm.Opcodes.PUTSTATIC
import org.objectweb.asm.Opcodes.RETURN
import org.objectweb.asm.Type

class LooperPatcher {
    companion object {
        fun patch(): ByteArray {
            val cw = ClassWriter(0)
            var mv: MethodVisitor

            cw.visit(52, ACC_PUBLIC + ACC_SUPER, "android/os/Looper", null, "java/lang/Object", null)

                val fv = cw.visitField(ACC_PRIVATE + ACC_STATIC, "instance", "Landroid/os/Looper;", null, null)
                fv.visitEnd()

                mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
                mv.visitCode()
                mv.visitVarInsn(ALOAD, 0)
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
                mv.visitInsn(RETURN)
                mv.visitMaxs(1, 1)
                mv.visitEnd()

                mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getMainLooper", "()Landroid/os/Looper;", null, null)
                mv.visitCode()
                mv.visitFieldInsn(GETSTATIC, "android/os/Looper", "instance", "Landroid/os/Looper;")
                mv.visitInsn(ARETURN)
                mv.visitMaxs(1, 0)
                mv.visitEnd()

                mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null)
                mv.visitCode()
                mv.visitLdcInsn(Type.getType("Landroid/os/Looper;"))
                mv.visitMethodInsn(INVOKESTATIC, "org/mockito/Mockito", "mock", "(Ljava/lang/Class;)Ljava/lang/Object;", false)
                mv.visitTypeInsn(CHECKCAST, "android/os/Looper")
                mv.visitFieldInsn(PUTSTATIC, "android/os/Looper", "instance", "Landroid/os/Looper;")
                mv.visitInsn(RETURN)
                mv.visitMaxs(1, 0)
                mv.visitEnd()

            cw.visitEnd()

            return cw.toByteArray()
        }
    }
}
