package me.zhangjin.gradle.greeting

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.create

open class GreetingPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        // 使用插件扩展属性
        // key: greetingExt 要唯一
        val greetingExt = project.extensions.create("GreetingPlugin_greetingExt", GreetingPluginExtension::class.java)

        // 此处创建了一个容器
        val books = project.container<Book>()
        // 容器可以使用 all 进行遍历
        // 所有的 item 添加到这个集合时，也会执行这个 action
        books.all {
            sourceFile = project.file("src/docs/$name")
        }
        // 最后将容器添加到 project 的 extensions 集合中
        // 注意，此处要定义唯一的参数键 booksForInit
        project.extensions.add("booksForInit", books)

        project.tasks.create<Greeting>("greeting") {
            message = "Hello"
            recipient = "World"
            extension = greetingExt
        }
    }
}