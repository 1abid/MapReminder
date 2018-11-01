package com.bus.green.mapreminder.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.util.*


@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey @ColumnInfo(name = "id") val reminderID: String,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "message") val message: String
) {
    @Ignore
    constructor(reminderID: String = UUID.randomUUID().toString(), LatLng: LatLng, message: String) : this(
        reminderID,
        LatLng.longitude,
        LatLng.latitude,
        message)
}