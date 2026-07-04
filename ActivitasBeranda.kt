

package com.example.sewakosapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sewakosapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

      
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "pengguna"
        val role = intent.getStringExtra("EXTRA_ROLE") ?: "PENYEWA"
        binding.tvGreeting.text = "Halo, $username"

      
        val kos1 = Kos("K001", "Kos Melati", "Jl. Mawar No. 12").apply {
            tambahKamar(Kamar("A1", "Standar", 850000))
            tambahKamar(Kamar("A2", "Standar", 850000).also { it.updateStatusHuni(true) })
            tambahKamar(Kamar("A3", "VIP", 1200000))
        }
        val kos2 = Kos("K002", "Kos Anggrek", "Jl. Kenanga No. 3").apply {
            tambahKamar(Kamar("B1", "Standar", 750000))
            tambahKamar(Kamar("B2", "Standar", 750000).also { it.updateStatusHuni(true) })
        }

        val pemilik = PemilikProperti("U01", username, "password123").apply {
            tambahKos(kos1)
            tambahKos(kos2)
        }

     
        binding.tvTotalKos.text = pemilik.getDaftarKos().size.toString()
        binding.tvKamarKosong.text = pemilik.totalKamarKosong().toString()

        for (kos in pemilik.getDaftarKos()) {
            val kosong = kos.jumlahKamarKosong()
            val terisi = kos.tampilkanDaftarKamar().size - kosong

            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(Color.parseColor("#F1EFE8"))
                setPadding(24, 24, 24, 24)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 20 }
            }

            val title = TextView(this).apply {
                text = kos.namaKos
                textSize = 15f
                setTextColor(Color.parseColor("#000000"))
            }

            val alamat = TextView(this).apply {
                text = kos.alamat
                textSize = 12f
                setTextColor(Color.parseColor("#666666"))
            }

            val statusRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.START
                setPadding(0, 12, 0, 0)
            }

            val badgeKosong = TextView(this).apply {
                text = " $kosong kosong "
                textSize = 11f
                setTextColor(Color.parseColor("#27500A"))
                setBackgroundColor(Color.parseColor("#EAF3DE"))
            }
            val badgeTerisi = TextView(this).apply {
                text = " $terisi terisi "
                textSize = 11f
                setTextColor(Color.parseColor("#791F1F"))
                setBackgroundColor(Color.parseColor("#FCEBEB"))
                (layoutParams as? LinearLayout.LayoutParams)
                setPadding(16, 4, 16, 4)
            }
            badgeKosong.setPadding(16, 4, 16, 4)

            statusRow.addView(badgeKosong)
            val spacer = android.view.View(this).apply {
                layoutParams = LinearLayout.LayoutParams(12, 1)
            }
            statusRow.addView(spacer)
            statusRow.addView(badgeTerisi)

            card.addView(title)
            card.addView(alamat)
            card.addView(statusRow)

            binding.containerKos.addView(card)
        }
    }
}