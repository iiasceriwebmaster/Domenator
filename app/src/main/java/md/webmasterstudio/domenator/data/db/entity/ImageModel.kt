package md.webmasterstudio.domenator.data.db.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "image_tbl")
data class ImageModel(

    @PrimaryKey()
    @ColumnInfo(name = "idx")
    var idx: String = "",
    @ColumnInfo(name = "image_string")
    var imageString: String
)
