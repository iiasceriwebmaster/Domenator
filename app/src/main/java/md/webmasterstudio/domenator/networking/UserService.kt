package md.webmasterstudio.domenator.networking

import com.google.gson.JsonObject
import md.webmasterstudio.domenator.data.db.entity.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): User

    @GET("/get_lang")
    suspend fun getDictionary(): Response<JsonObject>
}