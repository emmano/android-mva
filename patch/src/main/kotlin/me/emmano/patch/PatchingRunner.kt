package me.emmano.patch

import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future

class PatchingRunner(private val clazz: Class<*>) : BlockJUnit4ClassRunner(clazz) {

    private val executorService =  Executors.newSingleThreadExecutor()

    override fun methodBlock(method: FrameworkMethod): Statement {
        super.methodBlock(method)

        return object : Statement() {
            override fun evaluate() {

                runOnTestThread(Runnable{
                    val parent = Thread.currentThread().contextClassLoader
                    Thread.currentThread().contextClassLoader =
                        InstrumentingClassLoader(clazz)
                    val loader = Thread.currentThread().contextClassLoader
                    val testClass = loader.loadClass(clazz.name)
                    val testMethod = testClass.getMethod(method.method.name)
                    val helper = HelperTestRunner(testClass)
                    val statement = helper.methodBlock(FrameworkMethod(testMethod))
                    try {
                        statement.evaluate()
                    } catch(e: Exception) {
                        throw e
                    } finally {
                        Thread.currentThread().contextClassLoader = parent
                    }
                })
            }
        }
    }

    fun runOnTestThread(runnable: Runnable) {
        runOnTestThread(Callable<Any?> {
            runnable.run()
            null
        })
    }

    fun <T> runOnTestThread(callable: Callable<T>): T {
        val future: Future<T> = executorService.submit(callable)
        return try {
            future.get()
        } catch (e: InterruptedException) {
            future.cancel(true)
            throw RuntimeException(e)
        } catch (e: ExecutionException) {
            throw e
        }
    }
}

class HelperTestRunner(sandboxTestClass: Class<*>) : BlockJUnit4ClassRunner(sandboxTestClass) {

    public override fun methodBlock(method: FrameworkMethod): Statement {
        return super.methodBlock(method)
    }

}