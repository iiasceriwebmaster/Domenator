package md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val name: String,
    val surname: String,
    val dateOfBirth: String
    //... other data fields that may be accessible to the UI
)