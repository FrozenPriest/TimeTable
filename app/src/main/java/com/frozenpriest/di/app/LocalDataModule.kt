package com.frozenpriest.di.app

import android.app.Application
import androidx.room.Room
import com.frozenpriest.data.local.MyRoomDatabase
import com.frozenpriest.data.local.dao.RecordsDao
import dagger.Module
import dagger.Provides

@Module
class LocalDataModule(val application: Application) {

    @AppScope
    @Provides
    fun provideDatabase(): MyRoomDatabase {
        return Room.databaseBuilder(
            application,
            MyRoomDatabase::class.java,
            "schedule_database"
        ).build()
    }

    @AppScope
    @Provides
    fun provideRecordsDao(database: MyRoomDatabase): RecordsDao = database.recordsDao()
}
