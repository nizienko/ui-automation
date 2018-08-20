package com.jetbrains.test.proxy

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.fixtures.Fixture
import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import java.io.Serializable
import java.lang.reflect.Method

/*
    experimental
 */

inline fun <reified T> wrap(remoteRobot: RemoteRobot, fixture: Fixture): T {
    return Enhancer.create(T::class.java, ComponentInterceptor(remoteRobot, fixture)) as T
}

class ComponentInterceptor(private val remoteRobot: RemoteRobot, private val fixture: Fixture): MethodInterceptor {
    override fun intercept(o: Any?, method: Method?, arguments: Array<out Any>?, methodProxy: MethodProxy?): Any {
        return if (method?.returnType == Any::class.java) {
            remoteRobot.execute(fixture) { _, c -> method.invoke(c, arguments) }
        } else {
            remoteRobot.retrieve(fixture) { _, c -> method?.invoke(c, arguments) as Serializable  }
        }
    }
}