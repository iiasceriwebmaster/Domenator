package md.webmasterstudio.domenator.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import md.webmasterstudio.domenator.data.db.dao.CarInfoDao
import md.webmasterstudio.domenator.data.db.dao.NotificationDao
import md.webmasterstudio.domenator.data.db.dao.ReportsInfoDao
import md.webmasterstudio.domenator.data.db.dao.UserDao
import md.webmasterstudio.domenator.data.db.entity.CarInfoEntity
import md.webmasterstudio.domenator.data.db.entity.NotificationEntity
import md.webmasterstudio.domenator.data.db.entity.ReportInfoEntity
import md.webmasterstudio.domenator.data.db.entity.User


@Database(entities = [User::class, CarInfoEntity::class, NotificationEntity::class, ReportInfoEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class DomenatorDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reportsDao(): ReportsInfoDao
    abstract fun notificationsDao(): NotificationDao
    abstract fun carInfoDao(): CarInfoDao

    companion object {
        @Volatile
        private var INSTANCE: DomenatorDatabase? = null

        fun getInstance(context: Context): DomenatorDatabase {
            if (INSTANCE == null) {
                synchronized(DomenatorDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DomenatorDatabase::class.java,
                            "domenator_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}

class Converters {

    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}