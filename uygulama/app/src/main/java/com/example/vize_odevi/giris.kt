package com.example.vize_odevi

// Gerekli kütüphaneler import ediliyor
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Yemek veri sınıfı oluşturuluyor. Her bir yemek için isim, malzeme ve tarif bilgilerini içerir.
data class Yemek(val isim: String, val malzeme: String, val tarif: String)

// `giris` adlı sınıf oluşturuluyor ve AppCompatActivity'den türetiliyor.
// Ayrıca `yemekekle.OnYemekEkleListener` arayüzünü uygular (yemek ekleme işlemleri için).
class giris : AppCompatActivity(), yemekekle.OnYemekEkleListener {

    // Yemek listesini gösterecek olan ListView tanımlanıyor.
    private lateinit var yemekListView: ListView

    // Varsayılan yemekler listesi. Sabit bir liste olarak tanımlanmış.
    private val defaultYemekList = listOf(
        Yemek(
            "Afyon Sucuk Ekmek",
            "Sucuk+Ekmek+İsteğe bağlı sebzeler",
            "Sucuk iyice kızarana kadar pişirilir daha sonrasında ekmek arasına koyun ve istediğiniz sebzeleri ekleyin."
        ),
        Yemek(
            "Sakala Çarpan Çorbası",
            "Yeşil Mercimek, Soğan, Erişte, Domates Salçası, Tereyağ, Zeytin Yağı, Tuz, Karabiber, Nane, Su",
            "Mercimekler sıcak suyla ıslatılıp bekletilir. Soğanlar öldürülür, domatesler eklenir, pişirilir. Salça eklenir, su ilave edilir. Erişte ve mercimek eklenerek erişteler şişene kadar pişirilir."
        ),
        Yemek(
            "Ağzı Açık",
            "Peynir, Maydanoz, Yumurta, Karabiber, Kıyma, Tuz",
            "Hamur yoğurulur, dinlendirilir. İç harcı eklenip şekil verilir. Yağlanır ve fırında pişirilir."
        )
    )

    // Kullanıcı tarafından eklenen yemeklerin tutulacağı liste.
    private val yeniYemekList = mutableListOf<Yemek>()

    // Yemek isimlerini gösterecek adapter tanımlanıyor.
    private lateinit var adapter: ArrayAdapter<String>

    // `onCreate` metodu uygulama başlatıldığında çalışır.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)

        // Yemek listesi ListView bağlanıyor.
        yemekListView = findViewById(R.id.yemekListView)

        // Varsayılan ve eklenen yemeklerin isimlerini içeren adapter oluşturuluyor.
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, // Basit bir liste öğesi tasarımı kullanılır.
            (defaultYemekList + yeniYemekList).map { it.isim } // Yemek isimlerini listeye ekler.
        )
        yemekListView.adapter = adapter // ListView'e adapter atanır.

        // ListView öğelerine tıklama işlemi tanımlanıyor.
        yemekListView.setOnItemClickListener { _, _, position, _ ->
            // Tıklanan yemeği belirler.
            val selectedYemek = if (position < defaultYemekList.size) {
                defaultYemekList[position] // Varsayılan listeden bir yemek seçilmişse.
            } else {
                yeniYemekList[position - defaultYemekList.size] // Kullanıcı tarafından eklenenlerden bir yemek seçilmişse.
            }
            showYemekDetails(selectedYemek) // Seçilen yemeğin detayları gösterilir.
        }
    }

    // Yemek detaylarını göstermek için yeni bir aktiviteye geçiş işlemini yapan metot.
    private fun showYemekDetails(yemek: Yemek) {
        val intent = Intent(this, YemekDetayActivity::class.java) // Yeni aktivite oluşturuluyor.
        intent.putExtra("yemekIsmi", yemek.isim) // Yemek ismi aktarılıyor.
        intent.putExtra("malzeme", yemek.malzeme) // Malzeme bilgisi aktarılıyor.
        intent.putExtra("tarif", yemek.tarif) // Tarif bilgisi aktarılıyor.
        startActivity(intent) // Yeni aktivite başlatılıyor.
    }

    // Menü öğelerini oluşturmak için çağrılan metot.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.yemek_list, menu) // Menü tasarımını bağlar.
        return true
    }

    // Menü öğelerine tıklama işlemlerini işleyen metot.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.oyun -> { // "Oyun" menü seçeneği tıklandığında.
                val intent = Intent(this, oyun::class.java) // Oyun aktivitesi başlatılır.
                startActivity(intent)
                return true
            }

            R.id.yemek_ekle -> { // "Yemek Ekle" menü seçeneği tıklandığında.
                val dialog = yemekekle() // Yemek ekleme dialogu gösterilir.
                dialog.show(supportFragmentManager, "AddFoodDialog")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // `yemekekle` dialogundan gelen verileri işleyen metot.
    override fun onYemekEkle(yemekIsmi: String, malzeme: String, tarif: String) {
        val yeniYemek = Yemek(yemekIsmi, malzeme, tarif) // Yeni bir yemek oluşturulur.
        yeniYemekList.add(yeniYemek) // Listeye eklenir.

        // Adapter'e yeni yemek listesi eklenir ve güncellenir.
        adapter.clear()
        adapter.addAll((defaultYemekList + yeniYemekList).map { it.isim })
        adapter.notifyDataSetChanged()

        // Kullanıcıya yemek ekleme işlemi başarıyla tamamlandığını belirten bir Toast mesajı gösterilir.
        Toast.makeText(this, "Yemek Eklendi: $yemekIsmi", Toast.LENGTH_SHORT).show()
    }
}
