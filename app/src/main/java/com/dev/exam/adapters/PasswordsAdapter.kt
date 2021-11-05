package com.dev.exam.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dev.exam.R
import com.dev.exam.data.PasswordRow
import com.dev.exam.databinding.PasswordRvRowBinding
import com.dev.exam.utils.LogosAPI.getLogoImg
import com.dev.exam.utils.LogosAPI.urlToLogo

class PasswordsAdapter(private val passwords: List<PasswordRow>) :
    RecyclerView.Adapter<PasswordsAdapter.PasswordViewHolder>() {

    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PasswordRvRowBinding.bind(itemView)
        fun bind(row: PasswordRow) = with(binding)
        {
            loginTV.text = row.login
            passwordTV.text = row.password
            urlTV.text = row.url
            Log.d("URL", row.url)
            Log.d("LOGO URL", urlToLogo(row.url))
            logoIV.getLogoImg(row.url)

            viewBtn.apply{
                val passwordType = passwordTV.inputType
                val visible = EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                setOnClickListener {
                    passwordTV.apply {
                        Log.d("Type", inputType.toString())
                        inputType = if(inputType==visible) passwordType else visible
                }
            }
        }

            copyBtn.setOnClickListener {
                with(ClipData.newPlainText("text", passwordTV.text)) {
                    val clipboard = passwordTV.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(this)
                }

                Toast.makeText(itemView.context, "Пароль скопирован", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PasswordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.password_rv_row, parent, false)
        return PasswordViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(passwords[position])
    }

    override fun getItemCount() = passwords.size

}