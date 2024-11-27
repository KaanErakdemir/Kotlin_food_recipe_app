package com.example.vize_odevi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class YemekDetayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_detay)

        // Intent ile gelen verileri al
        val yemekIsmi = intent.getStringExtra("yemekIsmi")
        val malzeme = intent.getStringExtra("malzeme")
        val tarif = intent.getStringExtra("tarif")

        // TextView'leri bul ve bilgileri ayarla
        findViewById<TextView>(R.id.yemekIsmiTextView).text = yemekIsmi
        findViewById<TextView>(R.id.malzemeTextView).text = malzeme
        findViewById<TextView>(R.id.tarifTextView).text = tarif
    }
}
