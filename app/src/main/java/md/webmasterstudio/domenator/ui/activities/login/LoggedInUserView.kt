package md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login

/**
 * UserEntity details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val name: String,
    val surname: String,
    val dateOfBirth: String,
    val email: String
)