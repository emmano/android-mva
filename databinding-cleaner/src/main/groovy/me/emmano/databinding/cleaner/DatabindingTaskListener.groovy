package me.emmano.databinding.cleaner
;

import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskState;

class DatabindingTaskListener implements TaskExecutionListener {
    def processed = false

    @Override
    void beforeExecute(Task task) {
        if(task.name == "kaptDebugKotlin" && !processed){
            processed = true
            def variables = []
            new File(".").eachFileRecurse { file ->
                if(file.path.contains("build") && file.name.contains("Binding.java")) {
                    def tmpFile = new File(file.path + ".tmp")
                    tmpFile.withWriter { w ->
                        def shouldOverride = true
                        // for each line in the input file
                        new File( file.path ).eachWithIndex {line, index ->
                            if(index == 2) {
                                w<< "import androidx.lifecycle.Lifecycle;" << '\n'
                                w<< "import androidx.lifecycle.LifecycleObserver;" << '\n'
                                w<< "import androidx.lifecycle.LifecycleOwner;" << '\n'
                                w<< "import androidx.lifecycle.OnLifecycleEvent;" << '\n'
                            }else if(line.contains("final") && !line.contains("protected")) {
                                w<< line.replace("final ", "") << '\n'
                                def lineWithVariable = line.split(" ")
                                def variable = lineWithVariable.last().replace(";","")
                                variables.add(variable)
                                println variables
                            } else if(line.contains("}") && shouldOverride) {
                                shouldOverride = false
                                w<< line << '\n'
                                w<<'\n' << "@Override" << '\n'
                                w<< 'public void setLifecycleOwner(@Nullable LifecycleOwner lifecycleOwner) {' <<'\n'
                                w<< 'super.setLifecycleOwner(lifecycleOwner);' <<'\n'
                                w<< 'lifecycleOwner.getLifecycle().addObserver(new LifecycleObserver() {' <<'\n'
                                w<< '@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)' <<'\n'
                                w<< 'public void onDestroy() {' <<'\n'
                                variables.each {
                                    w<< "$it = null;"<<'\n'
                                }
                                w<< '\n'<< '}' <<'\n'
                                w<< '});' <<'\n'
                                w<< '}' <<'\n'
                            }
                            else {
                                w<< line <<'\n'
                            }
                        }
                        shouldOverride = true
                        w.close()
                    }
                    variables.clear()

                    tmpFile.renameTo( file.path )

                }
            }
        }
    }

    @Override
    void afterExecute(Task task, TaskState state) {

    }
}
