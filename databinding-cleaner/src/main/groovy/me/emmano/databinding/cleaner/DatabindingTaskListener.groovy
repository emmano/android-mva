package me.emmano.databinding.cleaner

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class DatabindingTaskListener implements TaskExecutionListener {

    Project project

    DatabindingTaskListener(Project project) {
        this.project = project
    }

    @Override
    void beforeExecute(Task task) {}

    @Override
    void afterExecute(Task task, TaskState state) {
        if (task.name.contains("dataBindingGenBaseClasses")) {
            def variables = [:]
            def module = task.path.split(":")[1].toString()
            def moduleFolderName = project.childProjects[module].projectDir.toPath().last().toString()
            new File(moduleFolderName).eachFileRecurse { file ->
                if (file.path.contains("build") && file.name.contains("Binding.java")) {
                    def fileName = file.toPath().last().toString().split(".java").first()
                    def tmpFile = new File(file.path + ".tmp")
                    tmpFile.withWriter { w ->
                        def shouldOverride = true
                        // for each line in the input file
                        new File(file.path).eachWithIndex { line, index ->
                            if (index == 2) {
                                w << "import androidx.lifecycle.Lifecycle;" << '\n'
                                w << "import androidx.lifecycle.LifecycleObserver;" << '\n'
                                w << "import androidx.lifecycle.LifecycleOwner;" << '\n'
                                w << "import androidx.lifecycle.OnLifecycleEvent;" << '\n'
                                w << "import java.lang.reflect.Field;" << '\n'
                                w << "import java.lang.reflect.Modifier;" << '\n'
                                w << "import java.util.List;" << '\n'
                                w << "import java.util.ArrayList;" << '\n'
                            } else if (line.contains("final") || line.contains("protected") && !line.contains(fileName)) {
                                w << line.replace("final ", "") << '\n'
                                def lineWithVariable = line.split(" ")
                                def variable = lineWithVariable.last().replace(";", "")
                                if (variable.length() != 0) {
                                    variables[(variable)] = line.contains("RecyclerView")
                                    println variables
                                }
                            } else if (line.contains("}") && shouldOverride) {
                                shouldOverride = false
                                w << line << '\n'
                                w << '\n' << "@Override" << '\n'
                                w << 'public void setLifecycleOwner(@Nullable LifecycleOwner lifecycleOwner) {' << '\n'
                                w << 'super.setLifecycleOwner(lifecycleOwner);' << '\n'
                                w << 'lifecycleOwner.getLifecycle().addObserver(new LifecycleObserver() {' << '\n'
                                w << '@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)' << '\n'
                                w << 'public void onDestroy() {' << '\n'
                                variables.each {
                                    if(it.value){
                                        w << "$it.key"+ ".setAdapter(null);" << '\n'
                                    }
                                    w << "$it.key = null;" << '\n'
                                }

                                w << 'try {' << '\n'
                                w << 'Field root = ViewDataBinding.class.getDeclaredField("mRoot");' << '\n'
                                w << '   root.setAccessible(true);' << '\n'
                                w << 'Field modifiersField = Field.class.getDeclaredField( "accessFlags" );' << '\n'
                                w << 'modifiersField.setAccessible( true );' << '\n'
                                w << 'modifiersField.setInt( root, root.getModifiers() & ~Modifier.FINAL );' << '\n'
                                w << "root.set($fileName" + ".this, null);" << '\n'
                                w << '} catch (NoSuchFieldException | IllegalAccessException e) {' << '\n'
                                w << 'e.printStackTrace();' << '\n'
                                w << '}' << '\n'

                                w << 'List<String> boundViews = new ArrayList<>();' << '\n'
                                w << "Field[] fields = $fileName" +'Impl.class.getDeclaredFields();' << '\n'
                                w << 'for (Field field : fields) {' << '\n'
                                w << 'if(field.getName().contains("mboundView")){' << '\n'
                                w << 'boundViews.add(field.getName());' << '\n'
                                w << '}}' << '\n'

                                w << 'for (String boundViewField : boundViews) {' << '\n'
                                w << 'try {' << '\n'
                                w << "Field boundView = $fileName" + 'Impl.class.getDeclaredField(boundViewField);' << '\n'
                                w << 'boundView.setAccessible(true);' << '\n'
                                w << 'Field modifiersField = Field.class.getDeclaredField( "accessFlags" );' << '\n'
                                w << 'modifiersField.setAccessible( true );' << '\n'
                                w << 'modifiersField.setInt( boundView, boundView.getModifiers() & ~Modifier.FINAL );' << '\n'
                                w << "boundView.set($fileName" + ".this, null);" << '\n'
                                w << '} catch (NoSuchFieldException | IllegalAccessException e) {' << '\n'
                                w << 'e.printStackTrace();' << '\n'
                                w << '}' << '\n'
                                w << '\n' << '}' << '\n'
                                w << '}' << '\n'

                                w << '});' << '\n'
                                w << '}' << '\n'
                            } else {
                                w << line << '\n'
                            }
                        }
                        shouldOverride = true
                        w.close()
                    }
                    variables.clear()

                    tmpFile.renameTo(file.path)

                }
            }
        }
    }
}
