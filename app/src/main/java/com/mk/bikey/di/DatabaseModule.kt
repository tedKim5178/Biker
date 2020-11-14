package com.mk.bikey.di

import android.content.Context
import com.mk.bikey.database.BikerAppDatabase
import com.mk.bikey.database.RecordDao
import com.mk.bikey.model.Record
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): BikerAppDatabase {
        return BikerAppDatabase.getInstance(context)
    }

    @Provides
    fun provideRecordDao(db: BikerAppDatabase): RecordDao {
        return db.recordDao()
    }
}
