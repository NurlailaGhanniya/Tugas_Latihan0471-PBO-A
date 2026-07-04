
class AuthValidator {

    fun isValid(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            return false
        }

        if (username.length < 4 || username.contains(" ")) {
            return false
        }

        if (password.length < 8 || password.none { it.isDigit() }) {
            return false
        }

        return true
    }

    fun getErrorMessage(username: String, password: String): String {
        return when {
            username.isEmpty() || password.isEmpty() -> "Username dan password wajib diisi"
            username.length < 4 || username.contains(" ") -> "Username minimal 4 karakter, tanpa spasi"
            password.length < 8 -> "Password minimal 8 karakter"
            password.none { it.isDigit() } -> "Password harus mengandung minimal 1 angka"
            else -> "Data valid"
        }
    }
}