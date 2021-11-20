package com.github.ai.fprovider.data.serialization

interface Deserializer<T> {
    fun deserialize(data: String): T?
}