package md.webmasterstudio.domenator.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import md.webmasterstudio.domenator.data.db.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserEntity>

    @Query(
        "SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1"
    )
    fun findByName(first: String, last: String): UserEntity

    @Insert
    fun insertAll(vararg userEntities: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)
}