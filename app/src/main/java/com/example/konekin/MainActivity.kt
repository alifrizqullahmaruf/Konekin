package com.example.konekin

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityMainBinding
import com.example.konekin.model.Users
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Membuat instance dari ApiClient
        val client = ApiClient.getInstance()

        // Mengirim permintaan API untuk mendapatkan data pengguna
        val response = client.getAllUsers()

        val userList = ArrayList<String>()

        // Menggunakan enqueue untuk menangani permintaan secara asinkron
        response.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    // Jika respons sukses, mengekstrak data pengguna dan menambahkannya ke daftar
                    for (i in response.body()!!.data) {
                        userList.add(i.employeeName)
                    }

                    // Membuat adapter untuk ListView
                    val listAdapter = ArrayAdapter(
                        this@MainActivity,
                        R.layout.simple_list_item_1,
                        userList
                    )

                    // Mengatur adapter ListView dengan data pengguna
                    binding.lvNama.adapter = listAdapter
                } else {
                    // Jika respons tidak sukses, menampilkan pesan kesalahan
                    Toast.makeText(this@MainActivity, "Koneksi error", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                // Menangani kesalahan koneksi
                Toast.makeText(this@MainActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }
}
