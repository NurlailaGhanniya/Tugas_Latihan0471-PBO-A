
package com.example.sewakosapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sewakosapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var selectedRole: String = "PENYEWA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRolePenyewa.setOnClickListener {
            selectedRole = "PENYEWA"
            binding.btnRolePenyewa.setBackgroundColor(0xFFE6F1FB.toInt())
            binding.btnRolePemilik.setBackgroundColor(0xFFF1EFE8.toInt())
        }
        binding.btnRolePemilik.setOnClickListener {
            selectedRole = "PEMILIK"
            binding.btnRolePemilik.setBackgroundColor(0xFFE6F1FB.toInt())
            binding.btnRolePenyewa.setBackgroundColor(0xFFF1EFE8.toInt())
        }

        binding.btnLogin.setOnClickListener {
            val usernameInput = binding.etUsername.text.toString()
            val passwordInput = binding.etPassword.text.toString()

            val validator = AuthValidator()

            if (validator.isValid(usernameInput, passwordInput)) {
                val intentPindah = Intent(this, DashboardActivity::class.java)
                intentPindah.putExtra("EXTRA_USERNAME", usernameInput)
                intentPindah.putExtra("EXTRA_ROLE", selectedRole)
                startActivity(intentPindah)
                finish()
            } else {

                val pesanError = validator.getErrorMessage(usernameInput, passwordInput)
                Toast.makeText(this, pesanError, Toast.LENGTH_SHORT).show()
            }
        }
    }
}