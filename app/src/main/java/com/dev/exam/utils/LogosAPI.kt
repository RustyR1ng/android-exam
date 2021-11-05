package com.dev.exam.utils
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

object LogosAPI {
    const val LOGOS_API_URL = "https://api.faviconkit.com"

    fun urlToLogo(domainUrl: String) = "$LOGOS_API_URL/$domainUrl/144"

    fun ImageView.getLogoImg(domainUrl: String){
        Picasso.get()
            .load(urlToLogo(domainUrl))
           // .placeholder(R.drawable.ic_launcher_background)
            .into(this, object : Callback{
                override fun onSuccess() {
                    Log.d("PICASSO", "success")
                }

                override fun onError(e: Exception?) {
                    e?.printStackTrace()
                }
            })
    }


}