package com.github.ai.isfprovider.data.serialization

interface Deserializer<T> {
    fun deserialize(data: String): T?
}