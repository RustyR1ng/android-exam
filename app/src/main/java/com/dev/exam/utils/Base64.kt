package com.dev.exam.utils

import android.util.Base64.*
import com.dev.exam.utils.Base64.decodeB64

object Base64 {
    fun ByteArray.encodeB64(): String?{
        return try{
            encodeToString(this, DEFAULT)
        } catch (e: Exception) {
            null
        }
    }
    fun String.decodeB64(): ByteArray?{
        return try{
            decode(this, DEFAULT)
        } catch (e: Exception) {
            null
        }
    }
}