package com.jetbrains.test.data

import java.io.Serializable

class RemoteComponent(
        val id: String,
        val className: String? = null,
        val name: String? = null,
        val x: Int? = null,
        val y: Int? = null,
        val width: Int? = null,
        val height: Int? = null
): Serializable