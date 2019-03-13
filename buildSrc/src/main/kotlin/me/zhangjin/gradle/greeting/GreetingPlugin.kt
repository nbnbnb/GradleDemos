package me.zhangjin.gradle.greeting

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

open class GreetingPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        // 使用插件扩展属性
        // key: greetingExt 要唯一
        val greetingExt = project.extensions.create("GreetingPlugin_greetingExt", GreetingPluginExtension::class.java)

        project.tasks.create<Greeting>("greeting") {
            message = "Hello"
            recipient = "World"
            extension = greetingExt
        }
    }
}