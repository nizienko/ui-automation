package com.jetbrains.test.data

import org.apache.commons.io.IOUtils
import java.io.Serializable


class ObjectContainer(
        val className: String,
        val classBytes: ByteArray,
        val objectBytes: ByteArray
) : Serializable

fun Any.pack(): ObjectContainer {
    val klass = this::class.java
    val classBytes = IOUtils.toByteArray(klass.classLoader.getResourceAsStream(klass.name.replace(".", "/") + ".class"))
    val objectBytes = this.serializeToBytes()
    return ObjectContainer(klass.name, classBytes, objectBytes)
}

