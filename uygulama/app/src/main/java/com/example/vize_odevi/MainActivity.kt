package com.example.vize_odevi

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vize_odevi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // SharedPreferences, "Beni Hatırla" özelliği için kullanılacak
    private lateinit var sharedPreferences: SharedPreferences

    // Giriş başarılı olup olmadığını takip eden bir değişken
    private var isLoginSuccessful = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // SharedPreferences başlatılıyor, veriler "LoginPrefs" adlı dosyada saklanacak
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        // SQLite veritabanı açılıyor ve tablo oluşturuluyor
        val mydatabase = this.openOrCreateDatabase("Kullanici", MODE_PRIVATE, null)
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS kullanicibilgi(name VARCHAR, sifre INT)")

        // Giriş ekranındaki görsel bileşenler tanımlanıyor
        val usernameEditText = binding.usernameEditText
        val passwordEditText = binding.passwordEditText
        val rememberMeCheckBox = binding.rememberMeCheckBox
        val loginButton = binding.loginButton

        // SharedPreferences'tan daha önce kaydedilmiş kullanıcı adı ve şifre alınıyor
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")

        // Eğer kayıtlı kullanıcı bilgileri varsa, alanlar dolduruluyor ve checkbox işaretleniyor
        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            usernameEditText.setText(savedUsername)
            passwordEditText.setText(savedPassword)
            rememberMeCheckBox.isChecked = true // "Beni Hatırla" checkbox'ı işaretleniyor
        } else {
            rememberMeCheckBox.isChecked = false // Kayıtlı bilgi yoksa checkbox temizleniyor
        }

        // "Beni Hatırla" checkbox'ının durum değişikliği dinleniyor
        rememberMeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            if (isChecked) {
                // Eğer checkbox işaretlendiyse, kullanıcı bilgileri kaydediliyor
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                editor.putString("username", username)
                editor.putString("password", password)
            } else {
                // Eğer checkbox işaretlenmediyse, kaydedilmiş bilgiler siliniyor
                editor.remove("username")
                editor.remove("password")
            }
            editor.apply() // Değişiklikler uygulanıyor
        }

        // Giriş butonuna tıklanma işlemi tanımlanıyor
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString() // Kullanıcı adı alınıyor
            val password = passwordEditText.text.toString().toIntOrNull() // Şifre alınıyor (tam sayıya dönüştürülüyor)

            // Eğer şifre geçerli değilse hata mesajı gösteriliyor
            if (password == null) {
                Toast.makeText(this, "Lütfen geçerli bir şifre girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Veritabanında kullanıcı adı ve şifre sorgulanıyor
            val cursor: Cursor = mydatabase.rawQuery(
                "SELECT * FROM kullanicibilgi WHERE name = ? AND sifre = ?",
                arrayOf(username, password.toString()) // Parametreler güvenli bir şekilde veriliyor
            )

            if (cursor.moveToFirst()) {
                // Eğer kullanıcı bilgileri doğruysa
                isLoginSuccessful = true // Başarılı giriş bayrağı true olarak ayarlanıyor
                Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show()

                // "Beni Hatırla" seçeneği işaretli ise bilgiler kaydediliyor
                if (rememberMeCheckBox.isChecked) {
                    val editor = sharedPreferences.edit()
                    editor.putString("username", username)
                    editor.putString("password", password.toString())
                    editor.apply()
                }

                // Başka bir aktiviteye yönlendirme yapılıyor
                val intent = Intent(this, giris::class.java)
                startActivity(intent)
            } else {
                // Eğer bilgiler yanlışsa hata mesajı gösteriliyor
                Toast.makeText(this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show()
                isLoginSuccessful = false // Başarılı giriş bayrağı sıfırlanıyor
            }

            cursor.close() // Cursor kapatılıyor (hafıza sızıntısını önlemek için)
        }

        // Test verisi ekleniyor (isteğe bağlı)
        addTestUser(mydatabase)
    }

    override fun onResume() {
        super.onResume()

        // Eğer giriş sonrası geri dönülmüşse giriş bilgileri temizleniyor
        if (isLoginSuccessful) {
            clearLoginFields()
            isLoginSuccessful = false // Bayrak sıfırlanıyor
        }
    }

    // Test verisi eklemek için bir metot
    private fun addTestUser(mydatabase: android.database.sqlite.SQLiteDatabase) {
        try {
            val contentValues = ContentValues()
            contentValues.put("name", "Kaan") // Test kullanıcı adı
            contentValues.put("sifre", 123) // Test şifresi
            mydatabase.insert("kullanicibilgi", null, contentValues) // Veritabanına ekleniyor
        } catch (e: Exception) {
            e.printStackTrace() // Hata durumunda hata mesajı yazdırılıyor
        }
    }

    // Giriş alanlarını temizlemek için yardımcı bir metot
    private fun clearLoginFields() {
        binding.usernameEditText.setText("") // Kullanıcı adı temizleniyor
        binding.passwordEditText.setText("") // Şifre temizleniyor
        // "Beni Hatırla" durumu korunuyor, checkbox değiştirilmedi
    }
}
