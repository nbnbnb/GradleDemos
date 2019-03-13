package me.zhangjin.gradle.greeting

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

// 一定要 open
open class Greeting : DefaultTask() {
    var message: String? = null
    var recipient: String? = null
    lateinit var extension: GreetingPluginExtension

    @TaskAction
    fun sayGreeting() {
        println("${message}, ${recipient}, ${extension.extMessage}")
    }

}