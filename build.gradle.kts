import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  kotlin("jvm") version "1.4.20"
  java
  application
  id("com.github.johnrengelman.shadow") version "6.1.0"
}

project.version = "1.0.0"
project.group = "xyz.aesthetical"
project.setProperty("mainClassName", "xyz.aesthetical.lookupbot.Launcher")

val kotlinVersion = KotlinVersion.CURRENT.toString()

repositories {
  jcenter()
  mavenCentral()
  
  maven(url = "https://jitpack.io")
}

dependencies {
  // kotlin
  implementation(group = "org.jetbrains.kotlin", name = "kotlin-script-runtime", version = kotlinVersion)
  implementation(group = "org.jetbrains.kotlin", name = "kotlin-compiler-embeddable", version = kotlinVersion)
  implementation(group = "org.jetbrains.kotlin", name = "kotlin-script-util", version = kotlinVersion)
  implementation(group = "org.jetbrains.kotlin", name = "kotlin-scripting-compiler-embeddable", version = kotlinVersion)
  
  // discord shit
  implementation(group = "net.dv8tion", name = "JDA", version = "4.2.0_168")
  implementation(group = "com.github.Devoxin", name = "Flight", version = "2.0.8")
  implementation(group = "com.jagrosh", name = "jda-utilities", version = "3.0.4")
  
  // random ass shit
  implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
  implementation(group = "com.typesafe", name = "config", version = "1.4.0")
  implementation(group = "com.github.jkcclemens", name = "khttp", version = "0.1.0")
  implementation(group = "org.jsoup", name = "jsoup", version = "1.13.1")
}

application {
  mainClass.set("xyz.aesthetical.lookupbot.Launcher")
}

tasks.apply {
  withType<KotlinCompile> {
    kotlinOptions.suppressWarnings = true
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
  }
  
  withType<ShadowJar> {
    manifest.attributes.apply {
      put("Main-Class", application.getMainClass())
    }
  }
}