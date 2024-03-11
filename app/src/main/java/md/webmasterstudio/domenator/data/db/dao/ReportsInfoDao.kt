package md.webmasterstudio.domenator.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import md.webmasterstudio.domenator.data.db.entity.ReportInfoEntity

@Dao
interface ReportsInfoDao {
    @Query("SELECT * FROM reports_info")
    fun getAll(): List<ReportInfoEntity>

    @Query("SELECT * FROM reports_info WHERE car_id = :carID")
    fun getAllByCarId(carID: Int): List<ReportInfoEntity>

    @Insert
    fun insertAll(vararg reports: ReportInfoEntity)

    @Delete
    fun delete(report: ReportInfoEntity)
}