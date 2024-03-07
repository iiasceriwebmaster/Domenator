package md.webmasterstudio.domenator.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val name: String,
    val surname: String,
    val dateOfBirth: String
)