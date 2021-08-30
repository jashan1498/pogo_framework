package com.example.pogo_framework.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "pokemon_team")
data class CapturedModel(
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0,
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("captured_at") var capturedAt: String = "",
    @SerializedName("captured_lat_at") var capturedLatAt: Double = 0.0,
    @SerializedName("captured_long_at") var capturedLongAt: Double = 0.0
) : Serializable {
    constructor() : this(0, 0, "", "", 0.0, 0.0)
}
