package me.emmano.databinding.cleaner

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class DatabindingCleanerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.gradle.taskGraph.addTaskExecutionListener new DatabindingTaskListener(project)
    }
}
