

class ValidationException(message: String) : Exception(message)

open class User(
    id: String,
    username: String,
    password: String,
    val role: String
) {
    var id: String = id
        private set

    var username: String = username
        set(value) {
            require(value.length >= 4 && !value.contains(" ")) {
                "Username minimal 4 karakter dan tidak boleh mengandung spasi"
            }
            field = value
        }

    // Password private total, tidak ada getter publik sama sekali
    private var password: String = password
        set(value) {
            require(isPasswordStrong(value)) {
                "Password minimal 8 karakter dan harus mengandung minimal 1 angka"
            }
            field = value
        }

    init {
        require(username.length >= 4 && !username.contains(" ")) {
            "Username minimal 4 karakter dan tidak boleh mengandung spasi"
        }
        require(isPasswordStrong(password)) {
            "Password minimal 8 karakter dan harus mengandung minimal 1 angka"
        }
    }

    private fun isPasswordStrong(pass: String): Boolean {
        return pass.length >= 8 && pass.any { it.isDigit() }
    }

    /** Login hanya mengembalikan Boolean, password asli tidak pernah bocor keluar */
    fun login(inputPassword: String): Boolean {
        return inputPassword == password
    }

    fun gantiPassword(passwordLama: String, passwordBaru: String): Boolean {
        if (!login(passwordLama)) return false
        password = passwordBaru
        return true
    }
}


class Penyewa(
    id: String,
    username: String,
    password: String
) : User(id, username, password, role = "PENYEWA") {

    private val riwayatSewa: MutableList<Sewa> = mutableListOf()

    fun getRiwayatSewa(): List<Sewa> = riwayatSewa.toList()

    fun lihatKamarKosong(daftarKos: List<Kos>): List<Kamar> {
        return daftarKos.flatMap { it.tampilkanDaftarKamar() }.filter { !it.statusHuni }
    }

    fun sewaKamar(kamar: Kamar, tanggalMulai: String, tanggalSelesai: String): Sewa? {
        if (kamar.statusHuni) {
            println("Kamar ${kamar.kodeKamar} sedang dihuni, tidak bisa disewa.")
            return null
        }
        val sewaBaru = Sewa(
            kodeSewa = "SW-${System.currentTimeMillis()}",
            kamar = kamar,
            tanggalMulai = tanggalMulai,
            tanggalSelesai = tanggalSelesai,
            totalBiaya = kamar.hargaSewa
        )
        riwayatSewa.add(sewaBaru)
        kamar.updateStatusHuni(true)
        return sewaBaru
    }
}


class PemilikProperti(
    id: String,
    username: String,
    password: String
) : User(id, username, password, role = "PEMILIK") {

    private val daftarKos: MutableList<Kos> = mutableListOf()

    fun getDaftarKos(): List<Kos> = daftarKos.toList()

    fun tambahKos(kos: Kos) {
        daftarKos.add(kos)
    }

    fun totalKamarKosong(): Int {
        return daftarKos.sumOf { kos -> kos.tampilkanDaftarKamar().count { !it.statusHuni } }
    }
}


class Kos(
    kodeKos: String,
    namaKos: String,
    alamat: String
) {
    var kodeKos: String = kodeKos
        private set

    var namaKos: String = namaKos
        private set

    var alamat: String = alamat
        set(value) {
            require(value.isNotBlank()) { "Alamat tidak boleh kosong" }
            field = value
        }

    private val daftarKamar: MutableList<Kamar> = mutableListOf()

    fun tambahKamar(kamar: Kamar) {
        daftarKamar.add(kamar)
    }

    fun tampilkanDaftarKamar(): List<Kamar> = daftarKamar.toList()

    fun jumlahKamarKosong(): Int = daftarKamar.count { !it.statusHuni }
}


class Kamar(
    kodeKamar: String,
    tipeKamar: String,
    hargaSewa: Int
) {
    var kodeKamar: String = kodeKamar
        private set

    var tipeKamar: String = tipeKamar
        private set

    var hargaSewa: Int = hargaSewa
        set(value) {
            require(value > 0) { "Harga sewa tidak boleh minus atau nol: $value" }
            field = value
        }

    var statusHuni: Boolean = false
        private set

    init {
        require(hargaSewa > 0) { "Harga sewa tidak boleh minus atau nol: $hargaSewa" }
    }

    fun updateStatusHuni(status: Boolean) {
        statusHuni = status
    }
}


class Sewa(
    kodeSewa: String,
    val kamar: Kamar,
    tanggalMulai: String,
    tanggalSelesai: String,
    totalBiaya: Int
) {
    var kodeSewa: String = kodeSewa
        private set

    var tanggalMulai: String = tanggalMulai
        private set

    var tanggalSelesai: String = tanggalSelesai
        set(value) {
            require(value >= tanggalMulai) {
                "Tanggal selesai tidak boleh sebelum tanggal mulai"
            }
            field = value
        }

    var totalBiaya: Int = totalBiaya
        set(value) {
            require(value >= 0) { "Total biaya tidak boleh minus: $value" }
            field = value
        }

    var statusPembayaran: String = "BELUM_BAYAR"
        private set

    init {
        require(tanggalSelesai >= tanggalMulai) {
            "Tanggal selesai tidak boleh sebelum tanggal mulai"
        }
        require(totalBiaya >= 0) { "Total biaya tidak boleh minus: $totalBiaya" }
    }

    fun konfirmasiPembayaran() {
        statusPembayaran = "LUNAS"
    }

    fun batalkanSewa() {
        statusPembayaran = "DIBATALKAN"
        kamar.updateStatusHuni(false)
    }
}


