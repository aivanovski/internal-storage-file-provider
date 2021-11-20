package com.github.ai.fprovider.data.serialization

interface Serializer<T> {
    fun serialize(data: T): String
}