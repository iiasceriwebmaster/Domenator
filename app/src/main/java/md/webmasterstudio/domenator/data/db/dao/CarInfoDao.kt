package md.webmasterstudio.domenator.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import md.webmasterstudio.domenator.data.db.entity.CarInfoEntity

@Dao
interface CarInfoDao {
    @Query("SELECT * FROM car_info")
    fun getAll(): List<CarInfoEntity>

    @Query("SELECT * FROM car_info WHERE id = :id")
    fun getAllByCarId(id: Int): List<CarInfoEntity>

    @Insert
    fun insertAll(vararg cars: CarInfoEntity)

    @Delete
    fun delete(carInfoEntity: CarInfoEntity)
}