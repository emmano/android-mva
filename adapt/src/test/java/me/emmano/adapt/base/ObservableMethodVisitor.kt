package me.emmano.adapt.base

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ASM4
import org.objectweb.asm.Type
import org.objectweb.asm.commons.GeneratorAdapter
import org.objectweb.asm.commons.Method

class ObservableMethodVisitor(private val generator: GeneratorAdapter) : MethodVisitor(ASM4) {

    override fun visitCode() {
        generator.loadThis()
        val m =
            Method.getMethod("void <init> ()")

        generator.invokeConstructor(Type.getObjectType("java/lang/Object"), m)

        generator.visitVarInsn(ALOAD, 0)

        generator.newInstance(Type.getObjectType("java/util/ArrayList"))
        generator.dup()
        generator.invokeConstructor(Type.getObjectType("java/util/ArrayList"), m)
        generator.putField(Type.getObjectType("android/database/Observable"), "mObservers", Type.getObjectType("java/util/ArrayList"))
        generator.returnValue()
        generator.endMethod()



    }

}
