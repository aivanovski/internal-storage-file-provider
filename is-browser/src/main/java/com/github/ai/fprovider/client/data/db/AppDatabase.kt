package com.github.ai.fprovider.client.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.ai.fprovider.client.data.db.converters.DatabaseTypeConverters
import com.github.ai.fprovider.client.data.db.dao.ProxyFileReferenceDao
import com.github.ai.fprovider.client.data.db.entity.ProxyFileReference

@Database(
    entities = [
        ProxyFileReference::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun proxyFileReferenceDao(): ProxyFileReferenceDao
}