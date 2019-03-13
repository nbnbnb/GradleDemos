package me.zhangjin.gradle.greeting;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GreetingPlugin implements Plugin<Project> {

    public void apply(Project project) {

        // 使用插件扩展属性
        var greetingExt = project.getExtensions().create("greetingExt", GreetingPluginExtension.class);

        // greeting 表示任务的名称
        project.getTasks().create("greeting", Greeting.class, (task) -> {
            task.setMessage("Hello");
            task.setRecipient("World");
            task.setExtension(greetingExt);
        });
    }
}