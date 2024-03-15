package md.webmasterstudio.domenator.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import md.webmasterstudio.domenator.data.db.entity.ImageModel

@Dao
interface ImageDao {
//
//    @Query("select * from image_tbl")
//    suspend fun getAllImages(): List<ImageModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewImage(image: ImageModel)

}