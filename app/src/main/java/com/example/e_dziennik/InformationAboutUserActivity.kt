package com.example.e_dziennik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.io.InputStream

class InformationAboutUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_about_user)

        val isLogged = intent.getBooleanExtra("isLogged", false)

        if (!isLogged){
            val back = Intent(this, LoginActivity::class.java)
            startActivity(back)
            Toast.makeText(applicationContext, "Aby wyświetlić informacje o użytkowniku musisz być zalogowany!", Toast.LENGTH_LONG).show()
            finish()
        }
        else{
            val imie = findViewById<TextView>(R.id.name)
            val nazwisko = findViewById<TextView>(R.id.surname)
            val klasa = findViewById<TextView>(R.id.klasa)
            val nr_tel = findViewById<TextView>(R.id.phone_nbr)
            val email = findViewById<TextView>(R.id.email)
            val adres = findViewById<TextView>(R.id.addres)
            val pesel = findViewById<TextView>(R.id.pesel)
            val data_uro = findViewById<TextView>(R.id.birthday)

            val user_id = intent.getStringExtra("user_id")
            val inputStream: InputStream = resources.openRawResource(R.raw.dane_uzytkownika)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, charset("UTF-8"))
            val json = JSONObject(jsonString)
            val user = json.getJSONObject(user_id)

            val imie_val = user.getString("imie")
            val nazwisko_val = user.getString("nazwisko")
            val klasa_val = user.getString("klasa")
            val nr_tel_val = user.getString("nr_tel")
            val email_val = user.getString("mail")
            val adres_val = user.getString("adres_zam")
            val pesel_val = user.getString("pesel")
            val data_uro_val = user.getString("data_urodzenia")
            
            imie.text = imie_val
            nazwisko.text = nazwisko_val
            klasa.text = klasa_val
            nr_tel.text = nr_tel_val
            email.text = email_val
            adres.text = adres_val
            pesel.text = pesel_val
            data_uro.text = data_uro_val
        }
    }
}