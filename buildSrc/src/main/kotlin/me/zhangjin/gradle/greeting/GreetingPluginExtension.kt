package me.zhangjin.gradle.greeting

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.newInstance

// 设置插件扩展属性
// 一定要 open

// 注意，此处的 @javax.inject.Inject
// 注入了一个 ObjectFactory
open class GreetingPluginExtension @javax.inject.Inject constructor(objectFactory: ObjectFactory) {
    // 设置默认值
    // 默认值可以通过 config 进行重写
    var extMessage = "********************"

    // 实例化一个空 Person 对象
    val person: Person = objectFactory.newInstance()

    // 使用 DSL 风格的配置
    // 对 Person 对象进行设置
    fun build(action: Action<in Person>) {
        // 将这个初始化的 Person 对象
        // 传入 action 中进行配置
        action.execute(person)
    }
}
