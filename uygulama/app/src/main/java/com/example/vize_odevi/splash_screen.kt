package com.example.vize_odevi

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

class splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        val countdownTextView = findViewById<TextView>(R.id.textView4) // Sayaç için TextView

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // CountDownTimer ile geri sayım
        object : CountDownTimer(5000, 1000) { // 5 saniye, her bir adımda 1 saniye
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                countdownTextView.text = "$secondsLeft"
            }

            override fun onFinish() {
                // Sayaç bittiğinde MainActivity'e yönlendirme
                val intent = Intent(this@splash_screen, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}
