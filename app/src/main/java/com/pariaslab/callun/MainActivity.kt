package com.pariaslab.callun

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.callun.R
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //resources
        val moonPhaseIcon = findViewById<ImageView>(R.id.moonIcon)
        val moonPhaseTxt = findViewById<TextView>(R.id.moonPhaseTxt)

        val currentCalendar = Calendar.getInstance()
        var selectedDate = currentCalendar

        val selectDateEditText = findViewById<EditText>(R.id.selectDateEditText)

        // VER: reutilizar
        selectDateEditText.setText(getSelectedDateTxt(selectedDate))
        moonPhaseIcon.setImageResource(getMoonLapseIcon(selectedDate))
        moonPhaseTxt.text = String.format("%.0f %% del ciclo desde luna nueva", getMoonLapse(selectedDate) *100.0)

        selectDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = selectedCalendar

                    // VER: reutilizar
                    moonPhaseTxt.text = String.format("%.0f %% del ciclo desde luna nueva", getMoonLapse(selectedDate) *100.0)
                    selectDateEditText.setText(getSelectedDateTxt(selectedDate))
                    moonPhaseIcon.setImageResource(getMoonLapseIcon(selectedDate))
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}

private fun getSelectedDateTxt(date: Calendar): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date.time)
}