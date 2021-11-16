package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecordsResponse(
    @SerializedName("results")
    val records: List<Record>
)
