import me.zhangjin.gradle.greeting.Greeting
import me.zhangjin.gradle.greeting.GreetingPluginExtension

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
    // 只设置这个指定 task 的
    tasks.getByName<Greeting>("greeting") {
        message = "Hi"
    }

    // 配置自定义 Task 属性
    // withType 方式
    // 设置全部 Greeting 任务的
    tasks.withType<Greeting> {
        recipient = "Gradle from Parent"
    }

    // 配置插件扩展属性
    // 这个一定要在 apply 之后配置
    configure<GreetingPluginExtension> {
        extMessage = "<ExtMessage> Hi,Gradle"
    }

    // 根据 Task 来注册 Task
    // 而不是通过在 Plugin 中注册
    // 注册一个任务 fileTask
    tasks.register<Greeting>("greeting02") {
        // 由于之前是在 Plugin 中设置的 Task 属性
        // 所以在此处需要自己设置
        message = "AAA"
        // 通过 tasks.withType<Greeting> 全局设置
        recipient = "KKK"
        extension = GreetingPluginExtension()

        //dependsOn("")

        doLast {
            println("greeting02 ended ...")
        }
    }

}

// 实例 3，DSL 功能
open class Person {
    var name: String? = null
}

// 注意，此处的 @javax.inject.Inject
// 注入了一个 ObjectFactory
open class PersonPluginExtension @javax.inject.Inject constructor(objectFactory: ObjectFactory) {
    var message: String? = null
    // 此处指定了要实例化一个 Person 对象
    val person: Person = objectFactory.newInstance()

    fun show(action: Action<in Person>) {
        // 将这个初始化的 Person 对象
        // 传入 action 中进行配置
        action.execute(person)
    }
}


class PersonPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Create the 'greeting' extension
        val jack = project.extensions.create<PersonPluginExtension>("jack")
        project.task("personShow") {
            doLast {
                println("${jack.message} from ${jack.person.name}")
            }
        }
    }
}

apply<PersonPlugin>()

// apply 后才能进行配置
configure<PersonPluginExtension> {
    message = "Person Show"
    // 配置 Person 对象
    show {
        name = "jack"
    }
}

// 示例 4，DSL
class Book(val name: String) {
    var sourceFile: File? = null
}

class DocumentationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 此处创建了一个容器
        val books = project.container<Book>()
        // 容器可以使用 all 进行遍历
        books.all {
            sourceFile = project.file("src/docs/$name")
        }
        // 最后将容器添加到 project 的 extensions 集合中
        // 注意，此处要定义唯一的参数键 booksForInit
        project.extensions.add("booksForInit", books)
    }
}

apply<DocumentationPlugin>()

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