package com.example.konekin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityUserDetailBinding
import com.example.konekin.model.UserDetail
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id == -1) {
            Toast.makeText(this, "ID tidak valid", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        ApiClient.getInstance().getUserById(id).enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val user = body.data
                    binding.tvId.text = "ID: ${user.id}"
                    binding.tvName.text = "Nama: ${user.employeeName}"
                    binding.tvAge.text = "Umur: ${user.employeeAge}"
                    binding.tvSalary.text = "Gaji: ${user.employeeSalary}"
                } else {
                    Toast.makeText(
                        this@UserDetailActivity,
                        "Server error: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                Toast.makeText(this@UserDetailActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }
}