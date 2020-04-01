package me.emmano.patch

import me.emmano.patch.patching.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

class InstrumentingClassLoader(private val testClazz: Class<*>) : ClassLoader() {

    val alsoAcquire by lazy {  testClazz.getDeclaredAnnotation(Patch::class.java).add.javaObjectType.`package`!!.name}


    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*> {


        val findLoadedClass = findLoadedClass(name)
        if (findLoadedClass != null) return findLoadedClass

        if (name.contains("MockMethodDispatcher")) {
            return parent.loadClass(name)
        }

        if (name == "org.mockito.configuration.MockitoConfiguration") {
            throw ClassNotFoundException()
        }

        if (shouldAcquire(name)) {
            when (name) {
                "android.os.Looper" -> {
                    val bytes = LooperPatcher.patch()

                    return defineClass(name, bytes, 0, bytes.size)
                }
                "android.database.Observable" -> {
                    val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
                    val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    val classVisitor =
                        ObservableClassAdapter(writer)

                    reader.accept(classVisitor, 0)

                    val bytes = writer.toByteArray()

                    return defineClass(name, bytes, 0, bytes.size)
                }
                "android.view.LayoutInflater" -> {

                    val bytes = LayoutInflaterPatcher.patch()

                    return defineClass(name, bytes, 0, bytes.size)
                }
                "androidx.databinding.DataBindingUtil" -> {

                    val bytes = DataBindingUtilPatcher.patch()

                    return defineClass(name, bytes, 0, bytes.size)
                }
                else -> {
                    val bytes = getBytecode(name)
                    return defineClass(name, bytes, 0, bytes.size)
                }
            }
        } else {
            return parent.loadClass(name)
        }

    }

    private fun shouldAcquire(name: String) =
        name.contains("androidx.")
                || name.contains("android.")
                || name.contains("com.nh")
                || name.contains("org.mockito")
                || name.contains(testClazz.`package`!!.name)
                || if (testClazz.isAnnotationPresent(Patch::class.java)) name.contains(alsoAcquire) else false

    private fun getBytecode(name: String): ByteArray {
        val reader = ClassReader(getResourceAsStream(generateInternalClassName(name)))
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        reader.accept(writer, 0)
        return writer.toByteArray()
    }

    private fun generateInternalClassName(name: String): String {
        return name.replace(".", "/") + ".class"
    }
}