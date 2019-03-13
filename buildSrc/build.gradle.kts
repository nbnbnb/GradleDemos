plugins {
    groovy
    maven
    `kotlin-dsl`
}

dependencies {
    compile(gradleApi())
    compile(localGroovy())
}

// Maven 仓库的坐标
// <groupId>me.zhangjin.gradle.greeting</groupId>
// <artifactId>CustomPlugins</artifactId>
// <version>0.0.2</version>

group = "me.zhangjin.gradle.greeting"
version = "0.0.2"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<Upload>("uploadArchives") {
    repositories.withGroovyBuilder {
        "mavenDeployer" {
            "repository"("url" to "file://D://MavenRepository")
        }
    }
}

// 读取 kts 脚本相关的类文件
// 需要这个仓库
repositories {
    mavenLocal()
    maven { url = uri("http://maven.aliyun.com/nexus/content/groups/public/") }
    //mavenCentral()
}
