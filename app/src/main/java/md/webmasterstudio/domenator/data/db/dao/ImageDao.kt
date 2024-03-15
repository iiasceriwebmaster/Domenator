package md.webmasterstudio.domenator.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import md.webmasterstudio.domenator.data.db.entity.ImageModel

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewImage(image: ImageModel)

    @Query("SELECT * FROM image_tbl")
    suspend fun getAllImages(): List<ImageModel>

    @Query("SELECT * FROM image_tbl WHERE idx = :imageId")
    suspend fun getImageById(imageId: String): ImageModel?
}