package com.dev.exam

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.exam.data.constants.PASSWORDS_FILE_PATH
import com.dev.exam.databinding.ActivityMainBinding
import com.dev.exam.utils.AES256

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var failedToast: Toast
    private lateinit var pref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("login", MODE_PRIVATE)

        initPasswordInput()
        checkLogin()

        failedToast = Toast.makeText(this, "Введен направильный пароль", Toast.LENGTH_LONG)


        binding.loginBtn.setOnClickListener {
            val password = binding.passwordInput.text.toString()

            AES256.encrypt(password, password)?.let { it1 -> Log.d("Encrypt", it1) }
            Log.d("First Line", getKey(password))
            login(password)

        }
    }
    private fun login(password: String){
        Log.d("Password", password)

        if (password == getKey(password)) {
            val intent = Intent(this, LoggedActivity::class.java)
            pref.edit().putString("Password", password).apply()
            startActivity(intent)
            this.finish()
        } else {
            failedToast.show()
        }
    }

    private fun initPasswordInput() {
        binding.passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.loginBtn.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun getKey(key: String) =
        with(assets.open(PASSWORDS_FILE_PATH).bufferedReader().readLine())
        {
            AES256.decrypt(this, key.trimEnd())
        }

    private fun checkLogin()= with(pref.getString("Password", null)) {
        if (this != null) {
            login(this)
        }
    }
}