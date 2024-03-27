package md.webmasterstudio.domenator.data.db.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
@Entity(tableName = "car_info")
data class CarInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "licence_plate_nr") val licencePlateNr: String?,
    @ColumnInfo(name = "speedometer_value") val speedometerValue: Long?,
    @ColumnInfo(name = "photo_ids") val photoIds: List<String> = emptyList(),
    @ColumnInfo(name = "document_ids") val documentIds: List<String> = emptyList()
)