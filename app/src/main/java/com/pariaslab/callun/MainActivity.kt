package com.pariaslab.callun

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.callun.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var gestureDetector: GestureDetector
    private var swipeScaleFactor: Float = 0.1f // Scale factor for swipe displacement of days
    private var isScrolling = false // Indicates if there is a swipe in progress

    private lateinit var selectDateEditText: EditText
    private lateinit var moonPhaseIcon: ImageView
    private lateinit var moonPhaseTxt: TextView
    private lateinit var nextFMTxt: TextView
    private lateinit var moonPhaseTxtData: TextView

    private val currentCalendar: Calendar = Calendar.getInstance()
    private var selectedDate: Calendar = currentCalendar

    companion object {
        const val REQUEST_SETTINGS = 1
        const val DEFAULT_SWIPE_SCALE_FACTOR = 0.1f
        const val SHARED_PREFS_NAME = "CallunPrefs"
        const val SWIPE_SCALE_FACTOR_KEY = "SwipeScaleFactor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recuperar el valor de swipeScaleFactor de las preferencias compartidas
        val sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        swipeScaleFactor = sharedPrefs.getFloat(SWIPE_SCALE_FACTOR_KEY, DEFAULT_SWIPE_SCALE_FACTOR)

        moonPhaseIcon = findViewById(R.id.moonIcon)
        moonPhaseTxt = findViewById(R.id.moonPhaseTxt)
        nextFMTxt = findViewById(R.id.nextFMTxt)
        moonPhaseTxtData = findViewById(R.id.moonPhaseTxtData)
        selectDateEditText = findViewById(R.id.selectDateEditText)

        updateUi(selectedDate)

        // Gestures
        gestureDetector = GestureDetector(this, this)

        moonPhaseIcon.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            handleTouchEvent(event)
            true
        }

        selectDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    //val selectedCalendar = Calendar.getInstance()
                    //selectedCalendar.set(year, month, dayOfMonth)
                    //selectedDate = selectedCalendar
                    selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    showTimePickerDialog()

                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

    }

    private fun showTimePickerDialog() {
        val currentHour = selectedDate.get(Calendar.HOUR_OF_DAY)
        val currentMinute = selectedDate.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                // Actualizar la hora seleccionada en el objeto Calendar
                selectedDate.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                // Mostrar la fecha y hora seleccionadas
                updateUi(selectedDate)                },
            currentHour,
            currentMinute,
            false
        )

        timePickerDialog.show()
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    private fun handleTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Action on screen touch
                isScrolling = true // start swipe
            }

            MotionEvent.ACTION_UP -> {
                // Action when user lift finger
                isScrolling = false // stop swipe
            }
        }
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (isScrolling) { // Verify if the user is scrolling
            val daysToMove = (distanceX * swipeScaleFactor).toInt()
            selectedDate.add(Calendar.DAY_OF_MONTH, -daysToMove)
            updateUi(selectedDate)
        }
        return true
    }

    override fun onLongPress(e: MotionEvent) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun getSelectedDateTxt(date: Calendar): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(date.time)
    }


    private fun updateUi(date: Calendar) {

        val moonLapse = getMoonLapse(date)
        val moonPosition = getMoonPosition(date)
        val moonTimes = getMoonTimes(date)

        val ffm = getFullMoons(date).time
        val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm").format(ffm.time)

        selectDateEditText.setText(getSelectedDateTxt(date))
        moonPhaseIcon.setImageResource(getMoonLapseIcon(date))
        moonPhaseTxt.text =
            String.format("Superficie visible iluminada: %.0f %%",
                          moonLapse.fraction * 100.0)
        moonPhaseTxtData.text =
            String.format("Distancia a la tierra: %.0f km\n" +
                          "Altitud Sobre el horizonte: %.0f °",
                          moonPosition.distance,
                          moonPosition.altitude)

        nextFMTxt.text = "\nPróxima luna llena: \n" + formattedDate

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SETTINGS && resultCode == RESULT_OK) {
            data?.let {
                val swipeScaleFactor =
                    it.getFloatExtra(SettingsActivity.EXTRA_SWIPE_SCALE_FACTOR, 0.1f)
                this.swipeScaleFactor = swipeScaleFactor
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, REQUEST_SETTINGS)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}

