package com.frozenpriest.data.remote

data class GetScheduleParams(
    val id: Int,
    val week: Int,
    val month: Int,
    val year: Int
)
