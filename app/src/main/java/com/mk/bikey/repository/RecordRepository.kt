package com.mk.bikey.repository

import com.mk.bikey.database.RecordDao
import com.mk.bikey.model.Record
import javax.inject.Inject

class RecordRepository @Inject constructor(
    private val recordDao: RecordDao
) {
    suspend fun createRecord(record: Record) {
        recordDao.insertRecord(record)
    }

    fun gerRecords() = recordDao.getRecordList()
}