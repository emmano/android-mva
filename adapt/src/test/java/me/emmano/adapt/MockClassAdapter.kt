package me.emmano.adapt

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.GeneratorAdapter

class MockClassAdapter(writer: ClassWriter) : ClassVisitor(
        Opcodes.ASM4, writer) {

    override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
    ): MethodVisitor {
        if (name.orEmpty() == "getMainLooper") {
            val visitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
            return MockMethodVisitor(
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