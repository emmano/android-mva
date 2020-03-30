package me.emmano.adapt.base

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import kotlin.reflect.KClass

class InstrumentingClassLoader(private val testClazz: Class<*>) : ClassLoader() {

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        val findLoadedClass = findLoadedClass(name)
        if (findLoadedClass != null) return findLoadedClass

        if (name == "android.os.Looper") {
            val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
            val classVisitor =
                LooperClassAdapter(writer)

            reader.accept(classVisitor, 0)

            val bytes = writer.toByteArray()

            return defineClass(name, bytes, 0, bytes.size)
        }

        if(name == "android.database.Observable") {
            val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
            val classVisitor =
                ObservableClassAdapter(writer)

            reader.accept(classVisitor, 0)

            val bytes = writer.toByteArray()

            return defineClass(name, bytes, 0, bytes.size)
        }

        if(name == "android.view.LayoutInflater") {
            val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
            val classVisitor =
                InflaterClassAdapter(writer)

            reader.accept(classVisitor, 0)

            val bytes = writer.toByteArray()

            return defineClass(name, bytes, 0, bytes.size)
        }
        val clazzToPatch = checkNotNull(testClazz.getAnnotation(Patch::class.java), {"Did you forget to specify @Patch?"}).clazz
        return if(name.contains(testClazz.`package`!!.name) || name.contains(clazzToPatch.javaObjectType.`package`!!.name) || name.contains("android.os.Handler"))  {
            val bytes = getBytecode(name)
            defineClass(name, bytes, 0, bytes.size)
        } else super.loadClass(name, resolve)
    }

    private fun getBytecode(name: String): ByteArray {
        val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)

        reader.accept(writer, 0)
        val file = File("$name.class")

        file.writeBytes(writer.toByteArray())
        return writer.toByteArray()
    }

    private fun generateInternalClassName(name: String) = name.replace(".", "/") + ".class"
}
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
        AnnotationTarget.CLASS
)
annotation class Patch(
        val clazz: KClass<*>
)
