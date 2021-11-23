package com.frozenpriest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import com.frozenpriest.data.local.entities.AvailablePeriodEntity
import com.frozenpriest.data.local.entities.AvailableStatusEntity
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.local.entities.DayEntity
import com.frozenpriest.data.local.entities.DoctorEntity
import com.frozenpriest.data.local.entities.PatientEntity
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
    fun insertFullRecords(records: List<FullRecord>) {
        val types = mutableListOf<AvailableTypeEntity>()
        val patients = mutableListOf<PatientEntity>()
        val statuses = mutableListOf<AvailableStatusEntity>()
        val recordEntities = mutableListOf<RecordEntity>()
        records.forEach {
            types.addAll(it.availableTypes)
            patients.add(it.patientEntity)
            statuses.add(it.statusEntity)
            recordEntities.add(it.recordEntity)
        }

        insertTypes(types)
        insertPatients(patients)
        insertStatuses(statuses)
        insertRecords(recordEntities)
    }

    @Transaction
    @Query("select * from records where date >= :start and date <= :end order by date asc")
    fun getFullRecordsInPeriod(start: Long, end: Long): Flow<List<FullRecord>>

    @Transaction
    @Query("select * from records where date >= :start and date <= :end order by date asc")
    fun getFullRecordsInPeriodBlocking(start: Long, end: Long): List<FullRecord>

    @Transaction
    @Insert(onConflict = IGNORE)
    fun insertRecords(records: List<RecordEntity>)

    @Transaction
    @Insert(onConflict = IGNORE)
    fun insertPatients(records: List<PatientEntity>)

    @Insert(onConflict = IGNORE)
    fun insertStatuses(status: List<AvailableStatusEntity>)

    @Insert(onConflict = IGNORE)
    fun insertPeriods(status: List<AvailablePeriodEntity>)

    @Insert(onConflict = IGNORE)
    fun insertTypes(types: List<AvailableTypeEntity>)

    @Insert(onConflict = IGNORE)
    fun insertDoctor(doctorEntity: DoctorEntity)

    @Query("select * from doctors where id like :id limit 1")
    fun getDoctorById(id: String): DoctorEntity

    @Transaction
    fun getDoctorWithRecordsInPeriod(id: String, start: Long, end: Long): DoctorWithRecords? {
        return DoctorWithRecords(
            doctor = getDoctorById(id),
            records = getFullRecordsInPeriodBlocking(start, end)
        )
    }

    @Query("select * from days where docId like :doctorId and date >= :start and date <= :end order by date asc")
    fun getDaysInPeriod(doctorId: String, start: Long, end: Long): List<DayEntity>

    @Query("select * from periods")
    fun getPeriods(): List<AvailablePeriodEntity>

    @Query("select * from types")
    fun getTypes(): List<AvailableTypeEntity>

    @Query("select * from statuses")
    fun getStatuses(): List<AvailableStatusEntity>

    @Insert(onConflict = IGNORE)
    fun insertDaySchedules(days: List<DayEntity>)
}
