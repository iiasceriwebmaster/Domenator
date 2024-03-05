package md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    var usernameError: Int? = null,
    var passwordError: Int? = null,
    val isDataValid: Boolean = false
)