package com.kiselev.utils

import java.io.File

/** Находит файл по расширению */
fun File.findFileByExtensions(extensions: List<String>): File? {
    return this.listFiles { file -> extensions.contains(file.extension) }?.toList()?.firstOrNull()
}

fun Long.bytesToMb() = this / 1024 / 1024

/** Возвращает свободное пространство в байтах */
fun getFreeSpace(): Long {
    val globalFile = File(".")
    return globalFile.freeSpace
}