package me.emmano.adapt.base

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import kotlin.reflect.KClass

open class InstrumentingClassLoader(private val testClazz: Class<*>) : ClassLoader(){

    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*> {

        val findLoadedClass = findLoadedClass(name)
        if (findLoadedClass != null) return findLoadedClass

        if(name.contains("MockMethodDispatcher") ){
            return super.loadClass(name, resolve)
        }
        if(name == "org.mockito.configuration.MockitoConfiguration") {
            throw ClassNotFoundException()
        }

        if(shouldAcquire(name)) {
            if (name == "android.os.Looper") {
                        val bytes = LooperPatcher.patch()


                return defineClass(name, bytes, 0, bytes.size)
            }

            else if (name == "android.database.Observable") {
                val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
                val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
                val classVisitor =
                        ObservableClassAdapter(writer)

                reader.accept(classVisitor, 0)

                val bytes = writer.toByteArray()

                return defineClass(name, bytes, 0, bytes.size)
            }

            else if (name == "android.view.LayoutInflater") {

                val bytes = LayoutInflaterPatcher.patch()

                return defineClass(name, bytes, 0, bytes.size)
            }

           else  if (name == "androidx.databinding.DataBindingUtil") {

                val bytes = DataBindingUtilPatcher.patch()

                return defineClass(name, bytes, 0, bytes.size)
            }
            else {
                val bytes = getBytecode(name)
                return defineClass(name, bytes, 0, bytes.size)
            }
        }else {
            return parent.loadClass(name)
        }

    }

    private fun shouldAcquire(name: String) =
            name.contains("androidx.")
                    || name.contains("android.")  || name.contains("com.nh") || (name.contains("org.mockito") )
                    || name.contains(testClazz.`package`!!.name)

    private fun getBytecode(name: String): ByteArray {
        val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)

        reader.accept(writer, 0)

        val file = File("$name.class")
        file.writeBytes(writer.toByteArray())
        return writer.toByteArray()
    }

    private fun generateInternalClassName(name: String) : String {
        val extension = if(name == "org.mockito.internal.creation.bytebuddy.MockMethodDispatcher" || name == "org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher" ) ".raw" else ".class"
        return  name.replace(".", "/") + extension}
}

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
        AnnotationTarget.CLASS
)
annotation class Patch(
        val clazz: KClass<*>
)

class SandboxClassLoader(clazz: Class<*>) : InstrumentingClassLoader(clazz)
