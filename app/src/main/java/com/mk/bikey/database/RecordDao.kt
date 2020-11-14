package com.mk.bikey.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mk.bikey.model.Record

@Dao
interface RecordDao {
    @Query("SELECT * FROM record")
    fun getRecordList(): LiveData<List<Record>>

    @Insert
    suspend fun insertRecord(record: Record): Long
}