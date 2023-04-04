package com.example.e_dziennik

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.reflect.typeOf

class WidokOcenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widok_ocen)

        val isLogged = intent.getBooleanExtra("isLogged", false)

        if (!isLogged){
            val back = Intent(this, LoginActivity::class.java)
            startActivity(back)
            Toast.makeText(applicationContext, "Aby wyświetlić oceny musisz być zalogowany!", Toast.LENGTH_LONG).show()
            finish()
        }
        else{
            val tableLayout = findViewById<TableLayout>(R.id.tabela_ocen)
            val user_id = intent.getStringExtra("user_id")
            val inputStream: InputStream = resources.openRawResource(R.raw.dane_uzytkownika)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, charset("UTF-8"))
            val json = JSONObject(jsonString)
            val user = json.getJSONObject(user_id)
            val oceny = user.getJSONObject("oceny")
            val iterator = oceny.keys()
            while (iterator.hasNext()) {
                val ocenyPrzedmiotu = mutableListOf<String>()
                val nazwaPrzedmiotu = iterator.next()
                val przedmiot = oceny.getJSONArray(nazwaPrzedmiotu)
                for (j in 0 until przedmiot.length()) {
                    ocenyPrzedmiotu.add(przedmiot.getString(j))
                }
                addTableRow(tableLayout, nazwaPrzedmiotu, ocenyPrzedmiotu)
                println(ocenyPrzedmiotu)
            }
        }
    }

    fun showMenu(view: View){
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_logged_out)
        popup.show()
    }

    fun onMenuItemClick(item: MenuItem?) {
        when(item?.itemId){
            R.id.logowanie -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.informacje -> {
                val user_id = intent.getStringExtra("user_id")
                val isLogged = intent.getBooleanExtra("isLogged", false)
                val intent = Intent(this, InformationAboutUserActivity::class.java)
                intent.putExtra("user_id", user_id)
                intent.putExtra("isLogged", isLogged)
                startActivity(intent)
                finish()
            }
            R.id.oceny -> {
                val intent = Intent(this, WidokOcenActivity::class.java)
                startActivity(intent)
                finish()
            }
            else-> Toast.makeText(applicationContext, "Błąd, Nie ma takiej opcji w menu!", Toast.LENGTH_SHORT).show()
        }
    }

    fun addTableRow(tableLayout: TableLayout, text1: String, oceny: MutableList<String>) {
        val tableRow = TableRow(tableLayout.context)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        tableRow.layoutParams = layoutParams
        tableRow.setBackgroundResource(R.drawable.table_border)
        layoutParams.height = dpToPx(60)

        val textView = TextView(tableLayout.context)
        val textViewParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            0.4f
        )
        textView.layoutParams = textViewParams
        textView.text = text1
        textView.setTextColor(Color.WHITE)
        textView.setBackgroundResource(R.drawable.table_border)
        textView.setPadding(15, 15, 15, 15)

        val textView2 = TextView(tableLayout.context)
        val textView2Params = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.MATCH_PARENT,
            0.6f
        )
        textView2.layoutParams = textView2Params
        textView2.setTextColor(Color.WHITE)
        textView2.setBackgroundResource(R.drawable.table_border)
        textView2.setPadding(15, 15, 15, 15)

        val sb = StringBuilder()
        for ((index, ocena) in oceny.withIndex()) {
            sb.append(ocena)
            if (index < oceny.lastIndex) {
                sb.append(", ")
            } else {
                sb.append(".")
            }
        }

        textView2.text = sb

        tableRow.addView(textView)
        tableRow.addView(textView2)

        tableLayout.addView(tableRow)
    }


    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}