package com.github.ai.fprovider.demo.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.ai.fprovider.demo.data.db.entity.ProxyFileReference

@Dao
interface ProxyFileReferenceDao {

    @Query("SELECT * FROM proxy_file_reference")
    fun getAll(): List<ProxyFileReference>

    @Query("SELECT * FROM proxy_file_reference WHERE uid = :uid")
    fun getByUid(uid: String): List<ProxyFileReference>

    @Query("SELECT * FROM proxy_file_reference WHERE path = :path")
    fun getByPath(path: String): List<ProxyFileReference>

    @Insert
    fun add(item: ProxyFileReference): Long

    @Query("DELETE FROM proxy_file_reference WHERE id = :id")
    fun remove(id: Long)

    @Query("DELETE FROM proxy_file_reference")
    fun removeAll()
}