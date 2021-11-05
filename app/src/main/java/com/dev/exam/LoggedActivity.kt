package com.dev.exam

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.exam.adapters.PasswordsAdapter
import com.dev.exam.data.PasswordRow
import com.dev.exam.data.constants.PASSWORDS_FILE_PATH
import com.dev.exam.databinding.ActivityLoggedBinding
import com.dev.exam.utils.AES256

class LoggedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoggedBinding
    private lateinit var passwordsList: List<PasswordRow>
    private lateinit var pref: SharedPreferences
    private lateinit var password: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("login", MODE_PRIVATE).apply {

            password = getString("Password", "")!!
        }


        passwordsList = getPasswords(password)
        initRV()


        binding.logutBtn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        pref.edit().remove("Password").apply()
        startActivity(intent)
        finish()
    }

    private fun initRV() = with(binding.recyclerView)
    {
        layoutManager = LinearLayoutManager(this@LoggedActivity)
        adapter = PasswordsAdapter(passwordsList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu_btn -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getPasswords(password: String) =
        assets.open(PASSWORDS_FILE_PATH)
            .bufferedReader()
            .lineSequence()
            .drop(1)
            .flatMap { encodedStr ->
                val rows = AES256.decrypt(encodedStr, password).split("\n")
                rows.map {
                    it.split(":").let { data ->
                        if (data.size!=3) return@map null

                        PasswordRow(
                            login = data[0],
                            password = data[1],
                            url = data[2]
                        )
                    }
                }
            }
            .toList().filterNotNull()
}



