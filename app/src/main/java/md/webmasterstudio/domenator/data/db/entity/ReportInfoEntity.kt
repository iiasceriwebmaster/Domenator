package md.webmasterstudio.domenator.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports_info")
data class ReportInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    @ColumnInfo(name = "car_id") val carId: Long?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "fuel_amount") val fuelAmount: Float?,
    @ColumnInfo(name = "speedometer_value") val speedometerValue: Long?,
    @ColumnInfo(name = "fuel_price") val fuelPrice: Float?
)