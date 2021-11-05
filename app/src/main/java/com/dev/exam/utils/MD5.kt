package com.dev.exam.utils

import java.math.BigInteger
import java.security.MessageDigest

object MD5 {
    fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
    }
}