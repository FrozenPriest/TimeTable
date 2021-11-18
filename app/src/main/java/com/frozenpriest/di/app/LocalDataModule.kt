package com.frozenpriest.di.app

import android.content.Context
import androidx.room.Room
import com.frozenpriest.data.local.MyRoomDatabase
import com.frozenpriest.data.local.dao.RecordsDao
import dagger.Module
import dagger.Provides

@Module
class LocalDataModule {

    @AppScope
    @Provides
    fun provideDatabase(app: Context) =
        Room.databaseBuilder(
            app,
            MyRoomDatabase::class.java,
            "schedule_database"
        ).build()

    @AppScope
    @Provides
    fun provideRecordsDao(database: MyRoomDatabase): RecordsDao = database.recordsDao()
}
