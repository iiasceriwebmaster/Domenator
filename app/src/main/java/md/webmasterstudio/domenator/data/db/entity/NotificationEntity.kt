package md.webmasterstudio.domenator.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?,
    @ColumnInfo(name = "user_did_read") val userDidRead: Boolean? = false,
    var isExpanded: Boolean = false
)