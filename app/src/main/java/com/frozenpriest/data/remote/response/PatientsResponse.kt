package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class PatientsResponse(
    @SerializedName("results")
    val records: List<Patient>
)
