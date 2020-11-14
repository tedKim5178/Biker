package com.mk.bikey.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mk.bikey.model.Record
import com.mk.bikey.support.converter.Converters

@Database(entities = [Record::class], version = 1)
@TypeConverters(Converters::class)
abstract class BikerAppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var instance: BikerAppDatabase? = null

        fun getInstance(context: Context): BikerAppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): BikerAppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BikerAppDatabase::class.java,
                "biker-db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).build()
        }
    }
}