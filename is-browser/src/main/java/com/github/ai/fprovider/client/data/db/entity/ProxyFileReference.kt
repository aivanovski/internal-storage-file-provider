package com.github.ai.fprovider.client.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "proxy_file_reference")
data class ProxyFileReference(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,

    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "path")
    val path: String,

    @ColumnInfo(name = "created")
    val created: Date
)