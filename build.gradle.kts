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

// 测试
// 在根目录下执行 gradle greeting 将会执行两个任务，跟目录任务，和子目录任务

// 问题：子项目中不能执行 gradle greeting 命令（找不到插件？）