package com.frozenpriest.data.local.converters

import com.frozenpriest.data.local.entities.AvailablePeriodEntity
import com.frozenpriest.data.local.entities.AvailableStatusEntity
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.local.entities.PatientEntity
import com.frozenpriest.data.local.entities.RecordEntity
import com.frozenpriest.data.local.entities.query.FullRecord
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import com.frozenpriest.data.remote.response.Patient
import com.frozenpriest.domain.model.Record
import com.frozenpriest.domain.model.RecordType

fun AvailablePeriod.toEntity(): AvailablePeriodEntity {
    return AvailablePeriodEntity(
        id = this.id,
        start = this.start,
        end = this.end
    )
}

fun AvailableStatus.toEntity(): AvailableStatusEntity {
    return AvailableStatusEntity(
        id = this.id,
        name = this.name
    )
}

fun AvailableType.toEntity(): AvailableTypeEntity {
    return AvailableTypeEntity(
        id = this.id,
        name = this.name
    )
}

fun AvailablePeriodEntity.fromEntity(): AvailablePeriod {
    return AvailablePeriod(
        id = this.id,
        start = this.start,
        end = this.end
    )
}

fun AvailableStatusEntity.fromEntity(): AvailableStatus {
    return AvailableStatus(
        id = this.id,
        name = this.name
    )
}

fun AvailableTypeEntity.fromEntity(): AvailableType {
    return AvailableType(
        id = this.id,
        name = this.name
    )
}

fun FullRecord.toRecord(): Record {
    return Record(
        id = this.recordEntity.id,
        status = this.statusEntity.fromEntity(),
        types = this.availableTypes.map { it.fromEntity() },
        patient = this.patientEntity.fromEntity(),
        reason = this.recordEntity.reason,
        room = this.recordEntity.room,
        start = this.recordEntity.start,
        end = this.recordEntity.end,
        recordType = RecordType.OCCUPIED,
        backgroundColor = null,
        dividerColor = null
    )
}

fun Patient.toEntity(): PatientEntity {
    return PatientEntity(
        id, medId, name, phone
    )
}

fun Record.toFullRecord(
    date: Long,
    dayId: String,
): FullRecord {
    return FullRecord(
        recordEntity = this.toEntity(date, dayId),
        statusEntity = this.status?.toEntity() ?: throw NullPointerException("No"),
        patientEntity = this.patient?.toEntity() ?: throw NullPointerException("No"),
        availableTypes = this.types?.map { it.toEntity() } ?: throw NullPointerException("No"),

    )
}

fun Record.toEntity(date: Long, dayId: String): RecordEntity {
    return RecordEntity(
        id = this.id ?: throw NullPointerException("No"),
        statusId = this.status?.id ?: throw NullPointerException("No"),
        types = this.types?.map { it.id } ?: throw NullPointerException("No"),
        patientId = this.patient?.id ?: throw NullPointerException("No"),
        reason = this.reason ?: throw NullPointerException("No"),
        room = this.room ?: throw NullPointerException("No"),
        date = date,
        start = this.start,
        end = this.end,
        dayId = dayId
    )
}

private fun PatientEntity.fromEntity(): Patient {
    return Patient(
        id = this.id,
        medId = this.medId,
        name = this.name,
        phone = this.phone
    )
}
