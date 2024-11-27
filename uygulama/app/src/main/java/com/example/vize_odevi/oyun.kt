package com.example.vize_odevi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class oyun : AppCompatActivity() {

    private val wordList = listOf(
        "apple", "banana", "orange", "grape", "melon", "peach", "pear", "plum", "kiwi", "lemon",
        "cat", "dog", "fish", "bird", "horse", "mouse", "duck", "cow", "sheep", "goat",
        "house", "car", "tree", "door", "window", "bed", "chair", "table", "book", "clock",
        "red", "blue", "green", "yellow", "white", "black", "pink", "brown", "gray", "purple",
        "sun", "moon", "star", "sky", "rain", "cloud", "snow", "wind", "fire", "water"
    )
    // Kelime listesi
    private var currentWord = "" // Şu anki kelime
    private var score = 0 // Puan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oyun) // Doğru layout dosyasını ayarlayın

        // Oyun için elemanları tanımlıyoruz
        val textView = findViewById<TextView>(R.id.textView) // Karışık kelime
        val editText = findViewById<EditText>(R.id.editTextText) // Kullanıcı tahmini
        val button = findViewById<Button>(R.id.button) // Kontrol butonu
        val scoreTextView = findViewById<TextView>(R.id.textView2) // Puan alanı

        // Yeni bir kelimeyi başlat
        startNewWord(textView)

        // Buton tıklama olayını tanımla
        button.setOnClickListener {
            val userGuess = editText.text.toString().trim() // Kullanıcının tahmini
            if (userGuess.equals(currentWord, ignoreCase = true)) {
                score += 10 // Puan artır
                Toast.makeText(this, "Doğru bildiniz! +10 puan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Yanlış! Doğru cevap: $currentWord", Toast.LENGTH_SHORT).show()
            }

            // Puan güncelle
            scoreTextView.text = "Puan: $score"

            // Yeni kelimeyi başlat
            startNewWord(textView)
            editText.text.clear() // Giriş alanını temizle
        }
    }

    private fun startNewWord(textView: TextView) {
        currentWord = wordList.random() // Rastgele kelime seç
        textView.text = currentWord.toList().shuffled().joinToString("") // Harfleri karıştır
    }
}
