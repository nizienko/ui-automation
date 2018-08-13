package com.jetbrains.test.services

import com.jetbrains.test.data.ObjectContainer
import com.jetbrains.test.data.deserialize


class LambdaLoader {
    // todo: check for className in classLoader
    val loadedClassesSet = HashSet<String>()
    inline fun <reified T> getFunction(container: ObjectContainer): T {
        if (loadedClassesSet.contains(container.className).not()) {
            loadClass(container.className, container.classBytes)
            loadedClassesSet.add(container.className)
        }
        val o = container.objectBytes.deserialize<Any>()
        return o as T
    }

    // todo: think how do it without reflection
    fun loadClass(name: String, bytes: ByteArray): Class<*> {
        val loader = this::class.java.classLoader
        val defineClassMethod = ClassLoader::class.java.declaredMethods.first {
            it.name == "defineClass" && it.parameterTypes.size == 4
        }
        defineClassMethod.isAccessible = true
        return defineClassMethod.invoke(loader, name, bytes, 0, bytes.size) as Class<*>
    }
}