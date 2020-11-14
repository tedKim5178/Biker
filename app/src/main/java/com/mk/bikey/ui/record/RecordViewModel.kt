package com.mk.bikey.ui.record

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mk.bikey.database.RecordDao
import com.mk.bikey.model.Record

class RecordViewModel @ViewModelInject constructor(
    val recordDao: RecordDao
) : ViewModel() {

    val records: LiveData<List<Record>> = recordDao.getRecordList()
}