package com.dev.exam.utils

import com.dev.exam.utils.Base64.decodeB64
import com.dev.exam.utils.Base64.encodeB64
import com.dev.exam.utils.MD5.toMD5
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AES256 {
    private fun cipher(opmode: Int, secretKey: String): Cipher {
        val secretKeyMD5 = secretKey.toMD5()
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(secretKeyMD5.toByteArray(Charsets.UTF_8), "AES")
        val iv = IvParameterSpec(secretKeyMD5.substring(0, 16).toByteArray(Charsets.UTF_8))
        c.init(opmode, sk, iv)
        return c
    }

    fun encrypt(str: String, secretKey: String): String? {
        val encrypted =
            cipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(str.toByteArray(Charsets.UTF_8))
        return encrypted.encodeB64()
    }

    fun decrypt(str: String, secretKey: String): String {
        val byteStr = str.decodeB64()
        return try {
            String(cipher(Cipher.DECRYPT_MODE, secretKey).doFinal(byteStr))
        } catch (e: Exception) {
            ""
        }
    }
}