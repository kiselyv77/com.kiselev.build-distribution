val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val koin_ktor_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "com.kiselev"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

val sshAntTask = configurations.create("sshAntTask")

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("dev.inmo:tgbotapi:7.1.2")
    implementation("dev.inmo:tgbotapi.utils:7.1.2")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    implementation("org.postgresql:postgresql:42.5.4")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_ktor_version")

    sshAntTask("org.apache.ant:ant-jsch:1.10.13")
}

ant.withGroovyBuilder {
    "taskdef"(
        "name" to "scp",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
        "classpath" to configurations.get("sshAntTask").asPath
    )
    "taskdef"(
        "name" to "ssh",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
        "classpath" to configurations.get("sshAntTask").asPath
    )
}

task("deploy") {
    dependsOn("clean", "shadowJar")
    ant.withGroovyBuilder {
        doLast {
            val knownHosts = File.createTempFile("knownhosts", "txt")
            val user = "root"
            val host = "11.111.11.1" //  // вместо 11.111.11.1 вставте ваш хост сервера
            val password = File("keys/password").readText()
            val jarFileName = "com.kiselev.build-distribution-all.jar"
            try {
                "scp"(
                    "file" to file("build/libs/$jarFileName"),
                    "todir" to "$user@$host:/root/build-distribution",
                    "password" to password,
                    "trust" to true,
                    "knownhosts" to knownHosts
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "password" to password,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "mv /root/build-distribution/$jarFileName /root/build-distribution/build-distribution.jar"
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "password" to password,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "systemctl stop build-distribution"
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "password" to password,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "systemctl start build-distribution"
                )
            } finally {
                knownHosts.delete()
            }
        }
    }
}
