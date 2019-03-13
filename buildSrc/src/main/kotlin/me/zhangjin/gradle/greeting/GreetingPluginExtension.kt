package me.zhangjin.gradle.greeting

// 设置插件扩展属性
// 一定要 open
open class GreetingPluginExtension {
    // 设置默认值
    // 默认值可以通过 config 进行重写
    var extMessage = "ext message from GreetingPlugin"
}
