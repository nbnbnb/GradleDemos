import me.zhangjin.gradle.greeting.Greeting
import me.zhangjin.gradle.greeting.GreetingPluginExtension
import me.zhangjin.gradle.greeting.Book

plugins {
    // 使用 plugin id 的方式来应用插件
    // 通过 buildSrc/src/main/resources/META-INF/gradle-plugins/me.zhangjin.gradle.greeting.properties
    // plugin id 引用自定义插件
    // *** 这个需要安装到本地仓库或 plugins.gradle.org 中 ***
    id("me.zhangjin.gradle.greeting") apply false
}



allprojects {

    // 设置全局扩展信息
    extra["greeting"] = "你好，世界！"

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

    // 配置自定义 Task 属性（需要指定 task 名称）
    // getByName 方式
    // 只设置这个指定 task 的属性
    tasks.getByName<Greeting>("greeting") {
        message = "Hi"
    }

    // 配置自定义 Task 属性
    // withType 方式
    // 设置全部 Greeting 任务的属性
    tasks.withType<Greeting> {
        recipient = "Gradle from Parent"
    }

    // 配置插件扩展属性
    // 这个一定要在 apply 之后配置
    // 只设置 plugin 的扩展属性
    // 对于通过 register<T> 方式创建的 Task，此处不会设置
    configure<GreetingPluginExtension> {
        extMessage = "<ExtMessage> Hi,Gradle"

        // 使用 DSL 风格的配置
        build {
            name = "jack"
        }
    }


    // 根据 Task 来注册 Task
    // 而不是通过在 Plugin 中注册
    // 注册一个任务 fileTask
    tasks.register<Greeting>("greetingByRegister") {
        // 由于之前是在 Plugin 中设置的 Task 属性
        // 所以在此处需要自己设置
        message = "AAA"
        // 通过 tasks.withType<Greeting> 全局设置
        // recipient = "KKK"
        extension = project.extensions.create("GreetingPlugin_greetingExt02", GreetingPluginExtension::class.java)
        extension.person.name = "jinjin"

        //dependsOn("")

        doLast {
            println("greeting02 ended ...")
        }
    }

}


// 通过 by（Delegate 语法）从 project.extensions 集合中获取 books
// 注意，此处的参数名称一定要是 booksForInit
val booksForInit: NamedDomainObjectContainer<Book> by project.extensions

booksForInit {
    // create 方法相对于调用 Book 的构造函数
    // 传递一个 name 参数
    create("quickStart") {
        // 重写了 sourceFile 属性
        sourceFile = file("src/docs/quick-start")
    }
    create("userGuide")
    create("developerGuide")
}

// 注册一个任务
// 显示所有的 books 信息
tasks.register("booksShow") {
    doLast {
        booksForInit.forEach { book ->
            println("${book.name} -> ${book.sourceFile}")
        }
    }
}
