package com.github.ai.isfprovider.data.serialization

interface Serializer<T> {
    fun serialize(data: T): String
}