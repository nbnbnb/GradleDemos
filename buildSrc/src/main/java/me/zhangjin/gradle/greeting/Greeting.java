package me.zhangjin.gradle.greeting;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class Greeting extends DefaultTask {
    private String message;
    private String recipient;
    private GreetingPluginExtension extension;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public GreetingPluginExtension getExtension() {
        return extension;
    }

    public void setExtension(GreetingPluginExtension extension) {
        this.extension = extension;
    }

    @TaskAction
    void sayGreeting() {
        System.out.printf("%s, %s!\n", getMessage(), getRecipient()); 
    }

}