package me.emmano.adapt.base

import org.mockito.Mockito
import org.objectweb.asm.ClassReader
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM4
import org.objectweb.asm.Type
import org.objectweb.asm.commons.GeneratorAdapter
import org.objectweb.asm.commons.Method
import org.objectweb.asm.tree.ClassNode

class LayoutInflaterMethodVisitor(private val generator: GeneratorAdapter) : MethodVisitor(ASM4

) {
    override fun visitCode() {
        val mockitoReader =
            ClassReader(Mockito::class.java.name)
        val mockitoClassNode =
            ClassNode(ASM4)
        mockitoReader.accept(mockitoClassNode, 0)

        val mockMethod = mockitoClassNode.methods.find { it.name == "mock" }!!

        generator.push(Type.getObjectType("android/view/LayoutInflater"))
        generator.invokeStatic(
            Type.getObjectType("org/mockito/Mockito"),
            Method("mock", mockMethod.desc)
        )
        generator.checkCast(Type.getObjectType("android/view/LayoutInflater"))

        generator.returnValue()
        generator.endMethod()
    }
}
