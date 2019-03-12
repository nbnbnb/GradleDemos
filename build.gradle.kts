import me.zhangjin.gradle.greeting.Greeting

plugins {
    // 使用 plugin id 的方式来应用插件
    // 通过 buildSrc/src/main/resources/META-INF/gradle-plugins/me.zhangjin.gradle.greeting.properties
    // plugin id 引用自定义插件
    // *** 这个需要安装到本地仓库或 plugins.gradle.org 中 ***
    id("me.zhangjin.gradle.greeting") apply false
}

allprojects {

    // 应用 buildScr 目录中的自定义插件（需要设置 me.zhangjin.gradle.greeting.properties）
    apply(plugin = "me.zhangjin.gradle.greeting")

    // 应用 buildScr 目录中的自定义插件（不用设置 me.zhangjin.gradle.greeting.properties）
    // 直接使用类名的方式
    // apply<me.zhangjin.gradle.greeting.GreetingPlugin>()

    // 设置子项目的仓库
    repositories {
        mavenLocal()
        maven { url = uri("http://maven.aliyun.com/nexus/content/groups/public/") }
    }

    // 配置自定义插件 getByName 方式
    tasks.getByName<Greeting>("greeting") {
        message = "Hi"
    }
}


// 配置自定义插件 withType 方式
tasks.withType<Greeting> {
    recipient = "Gradle from Parent"
}

// -------------------

// ------------------------

// 在 build.gradle.kts 中自定义插件
class GreetingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 使用插件扩展属性
        // 命名为 greeting
        val greeting = project.extensions.create<GreetingPluginExtension>("greeting")
        // Add a task that uses configuration from the extension object
        project.task("hello") {
            doLast {
                // 读取扩展属性的信息
                println(greeting.message)
            }
        }
    }
}


// 设置插件扩展属性
open class GreetingPluginExtension {
    var message = "Hello from GreetingPlugin"
}


// 应用自定义插件
apply<GreetingPlugin>()

// 重写插件扩展属性里面的信息
// 这个一定要在 apply 之后配置
configure<GreetingPluginExtension> {
    message = "Hi Gradle"
}

// -------- 自定义插件 02

// 创建一个默认 Task，继承 DefaultTask
open class GreetingToFileTask : DefaultTask() {

    lateinit var destination: String

    fun getDestination(): File {
        // 返回一个 java.io.File 对象
        return project.file(destination)
    }

    // 任务对象里面的任务
    // 用 @TaskAction 标记
    @TaskAction
    fun greet() {
        // 任务就是在指定的文件里面写入 Hello!
        val file = getDestination()
        file.parentFile.mkdirs()
        file.writeText("Hello!")
    }
}

// 注册一个任务 fileTask
tasks.register<GreetingToFileTask>("fileTask") {
    // 从 ext 属性中读取 greetingFile
    destination = project.extra["greetingFile"].toString()
}

// 注册一个任务 showHelloFile
tasks.register("showHelloFile") {
    // 依赖自定义的 fileTask 任务
    dependsOn("fileTask")
    doLast {
        // 依赖的任务执行完成之后，读取写入的内容
        println(file(project.extra["greetingFile"]!!).readText())
    }
}

// 在 ext 中设置 greetingFile
// buildDir 是 project 的一个属性
// 在 ./build 目录下会生成一个 hello.txt 文件
extra["greetingFile"] = "$buildDir/hello.txt"

// 测试
// 在根目录下执行 gradle greeting 将会执行两个任务，跟目录任务，和子目录任务

// 问题：子项目中不能执行 gradle greeting 命令（找不到插件？）