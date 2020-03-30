package me.emmano.adapt.base

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM4
import org.objectweb.asm.commons.GeneratorAdapter

class ObservableClassAdapter(writer: ClassWriter) : ClassVisitor(ASM4, writer) {

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        if (name == "<init>") {
            val visitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
            return ObservableMethodVisitor(
                GeneratorAdapter(
                    visitor,
                    access,
                    name,
                    descriptor
                )
            )
        } else {
            return super.visitMethod(access, name, descriptor, signature, exceptions)
        }
    }

}
