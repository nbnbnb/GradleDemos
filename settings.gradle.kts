rootProject.name = "GradleDemos"

include("HelloWrold")

project(":HelloWrold").projectDir = file("$rootDir/HelloWrold")

pluginManagement {
    resolutionStrategy {
    }
    repositories {
        mavenLocal()
        maven { url = uri("http://maven.aliyun.com/nexus/content/groups/public/") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}