package com.kiselev.utils

import java.security.MessageDigest

fun hashString(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(input.toByteArray())
    val hexString = StringBuilder()

    for (byte in digest) {
        val hex = Integer.toHexString(0xff and byte.toInt())
        if (hex.length == 1) hexString.append('0')
        hexString.append(hex)
    }

    return hexString.toString()
}

