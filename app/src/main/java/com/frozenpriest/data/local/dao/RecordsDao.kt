package com.frozenpriest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.frozenpriest.data.local.entities.AvailablePeriodEntity
import com.frozenpriest.data.local.entities.AvailableStatusEntity
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.local.entities.DoctorEntity
import com.frozenpriest.data.local.entities.RecordEntity
import com.frozenpriest.data.local.entities.query.DoctorWithRecords
import com.frozenpriest.data.local.entities.query.FullRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {
    @Transaction
    @Query("select * from records where id like :id limit 1")
    fun getFullRecord(id: String): Flow<FullRecord>

    @Transaction
    @Query("select * from records where date >= :start and date <= :end order by date asc")
    fun getFullRecordsInPeriod(start: Long, end: Long): Flow<List<FullRecord>>

    @Transaction
    @Query("select * from records where date >= :start and date <= :end order by date asc")
    fun getFullRecordsInPeriodBlocking(start: Long, end: Long): List<FullRecord>

    @Transaction
    @Insert
    fun insertRecords(records: List<RecordEntity>)

    @Insert
    fun insertStatuses(status: List<AvailableStatusEntity>)

    @Insert
    fun insertPeriods(status: List<AvailablePeriodEntity>)

    @Insert
    fun insertTypes(types: List<AvailableTypeEntity>)

    @Query("select * from doctors where id like :id limit 1")
    fun getDoctorById(id: String): DoctorEntity

    @Transaction
    fun getDoctorWithRecordsInPeriod(id: String, start: Long, end: Long): DoctorWithRecords {
        return DoctorWithRecords(
            doctor = getDoctorById(id),
            fullRecords = getFullRecordsInPeriodBlocking(start, end)
        )
    }
}
