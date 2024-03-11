package md.webmasterstudio.domenator.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_info")
data class CarInfo(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "licence_plate_nr") val licencePlateNr: String?,
    @ColumnInfo(name = "speedometer_value") val speedometerValue: Long?,
    @ColumnInfo(name = "car_photos") val carPhotos: List<String>? = mutableListOf(),
    @ColumnInfo(name = "document_photos") val documentPhotos: List<String>? = mutableListOf()
)