package com.pariaslab.callun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.callun.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var swipeScaleSeekBar: SeekBar
    private lateinit var swipeScaleLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        swipeScaleSeekBar = findViewById(R.id.swipeScaleSeekBar)
        swipeScaleLabel = findViewById(R.id.swipeScaleLabel)

        val currentSwipeScaleFactor = intent.getFloatExtra(EXTRA_SWIPE_SCALE_FACTOR, DEFAULT_SWIPE_SCALE_FACTOR)
        val currentSwipeScaleProgress = (currentSwipeScaleFactor * 100).toInt()
        swipeScaleSeekBar.progress = currentSwipeScaleProgress

        swipeScaleSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val newSwipeScaleFactor = progress / 100f
                swipeScaleLabel.text = "Valor de swipeScaleFactor: $newSwipeScaleFactor"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No se requiere ninguna acción al iniciar el seguimiento del SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val newSwipeScaleFactor = swipeScaleSeekBar.progress / 100f
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_SWIPE_SCALE_FACTOR, newSwipeScaleFactor)
                }
                setResult(Activity.RESULT_OK, resultIntent)
            }
        })

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            val newSwipeScaleFactor = swipeScaleSeekBar.progress / 100f
            val resultIntent = Intent().apply {
                putExtra(EXTRA_SWIPE_SCALE_FACTOR, newSwipeScaleFactor)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // Finalizar la actividad y regresar a MainActivity
        }
    }

    companion object {
        private const val DEFAULT_SWIPE_SCALE_FACTOR = 0.1f
        const val EXTRA_SWIPE_SCALE_FACTOR = "extra_swipe_scale_factor"
    }
}
