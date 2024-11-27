package com.example.vize_odevi

// Gerekli kütüphaneler import ediliyor
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

// Yeni yemek eklemek için bir DialogFragment sınıfı tanımlanıyor
class yemekekle : DialogFragment() {

    // Veri iletimi yapmak için bir arayüz oluşturuluyor
    interface OnYemekEkleListener {
        // Arayüzde yemek bilgilerini aktaran bir metot tanımlanıyor
        fun onYemekEkle(yemekIsmi: String, yemekDetayi: String, yemekMalzeme: String)
    }

    // Listener, bu sınıftan veri almak isteyen diğer bileşenler tarafından kullanılacak
    private var listener: OnYemekEkleListener? = null

    // Fragment, bağlı olduğu aktiviteye bağlandığında çağrılır
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Listener bağlanıyor ve kontrol ediliyor
        if (context is OnYemekEkleListener) {
            listener = context // Aktivite doğru türdeyse listener atanır
        } else {
            // Eğer aktivite, gerekli arayüzü uygulamamışsa hata fırlatılır
            throw RuntimeException("$context must implement OnYemekEkleListener")
        }
    }

    // Fragment'in görünümü oluşturuluyor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML dosyasından görünüm şişiriliyor
        val view = inflater.inflate(R.layout.yemekekle, container, false)

        // Görsel bileşenler tanımlanıyor
        val yemekIsimEditText = view.findViewById<EditText>(R.id.yemekisim) // Yemek adı EditText'i
        val yemekDetayEditText = view.findViewById<EditText>(R.id.edtFoodDetail) // Yemek detay EditText'i
        val yemekMalzemeEditText = view.findViewById<EditText>(R.id.tarif) // Yemek malzeme EditText'i
        val btnAddFood = view.findViewById<Button>(R.id.ekleButton) // "Ekle" butonu

        // "Ekle" butonuna tıklama işlemi tanımlanıyor
        btnAddFood.setOnClickListener {
            // EditText'lerden girilen değerler okunuyor
            val yemekIsmi = yemekIsimEditText.text.toString()
            val yemekDetayi = yemekDetayEditText.text.toString()
            val yemekMalzeme = yemekMalzemeEditText.text.toString()

            // Girilen değerlerin doğruluğu kontrol ediliyor
            if (yemekIsmi.isNotEmpty() && yemekDetayi.isNotEmpty()) {
                // Eğer alanlar doluysa, listener üzerinden veriler aktarılıyor
                listener?.onYemekEkle(yemekIsmi, yemekDetayi, yemekMalzeme)
                dismiss() // Dialog kapatılıyor
            } else {
                // Eğer alanlar boşsa, kullanıcıya hata mesajı gösteriliyor
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
            }
        }

        return view // Oluşturulan görünüm döndürülüyor
    }

    // Dialog'un genel ayarları yapılıyor
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Dialog varsayılan olarak oluşturuluyor
        val dialog = super.onCreateDialog(savedInstanceState)
        // Arka plan şeffaf hale getiriliyor
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}
