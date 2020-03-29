package me.emmano.adapt

import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

class LooperMockRunner(private val clazz: Class<*>) : BlockJUnit4ClassRunner(clazz) {
    val instrumentingClassLoader = InstrumentingClassLoader(
        clazz)
    override fun methodBlock(method: FrameworkMethod): Statement {
        super.methodBlock(method)

        return object : Statement() {
            override fun evaluate() {


                val testClass = instrumentingClassLoader.loadClass(clazz.name)
                val method = testClass.getMethod(method.method.name)
                val helper = HelperTestRunner(testClass)
                val statement = helper.methodBlock(FrameworkMethod(method))
                statement.evaluate()

            }
        }
    }
}

class HelperTestRunner(sandboxTestClass: Class<*>) : BlockJUnit4ClassRunner(sandboxTestClass) {

    public override fun methodBlock(method: FrameworkMethod): Statement {
        return super.methodBlock(method)
    }

}