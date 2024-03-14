package md.webmasterstudio.domenator.data.db.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_info")
data class CarInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "licence_plate_nr") val licencePlateNr: String?,
    @ColumnInfo(name = "speedometer_value") val speedometerValue: Long?,
    @ColumnInfo(name = "photo_paths") val photoPaths: List<String> = emptyList(),
    @ColumnInfo(name = "document_paths") val documentPaths: List<String> = emptyList()
)