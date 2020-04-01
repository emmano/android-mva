package me.emmano.adapt.base

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
import org.objectweb.asm.Opcodes.V1_8
import org.objectweb.asm.Type

class DataBindingUtilPatcher {
    
    companion object {
        fun patch() : ByteArray {
            val cw = ClassWriter(0)
            var mv: MethodVisitor

            cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, "androidx/databinding/DataBindingUtil", null, "java/lang/Object", null)

                val fv = cw.visitField(ACC_PRIVATE + ACC_STATIC, "binding", "Landroidx/databinding/ViewDataBinding;", null, null)
                fv.visitEnd()

                mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
                mv.visitCode()
                mv.visitVarInsn(ALOAD, 0)
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
                mv.visitInsn(RETURN)
                mv.visitMaxs(1, 1)
                mv.visitEnd()

                mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "inflate", "(Landroid/view/LayoutInflater;ILandroid/view/ViewGroup;Z)Landroidx/databinding/ViewDataBinding;", null, null)
                    val av = mv.visitParameterAnnotation(0, "Landroidx/annotation/NonNull;", false)
                    av.visitEnd()
        
                    val av1 = mv.visitParameterAnnotation(2, "Landroidx/annotation/Nullable;", false)
                    av1.visitEnd()
                mv.visitCode()
                mv.visitFieldInsn(GETSTATIC, "androidx/databinding/DataBindingUtil", "binding", "Landroidx/databinding/ViewDataBinding;")
                mv.visitInsn(ARETURN)
                mv.visitMaxs(1, 4)
                mv.visitEnd()

                mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null)
                mv.visitCode()
                mv.visitLdcInsn(Type.getType("Landroidx/databinding/ViewDataBinding;"))
                mv.visitMethodInsn(INVOKESTATIC, "org/mockito/Mockito", "mock", "(Ljava/lang/Class;)Ljava/lang/Object;", false)
                mv.visitTypeInsn(CHECKCAST, "androidx/databinding/ViewDataBinding")
                mv.visitFieldInsn(PUTSTATIC, "androidx/databinding/DataBindingUtil", "binding", "Landroidx/databinding/ViewDataBinding;")
                mv.visitInsn(RETURN)
                mv.visitMaxs(1, 0)
                mv.visitEnd()
            cw.visitEnd()

            return cw.toByteArray()
        }
    }

}