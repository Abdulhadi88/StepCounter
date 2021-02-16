package com.example.stepcounter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() , SensorEventListener{

    private var sensorManager : SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null){
            Toast.makeText(this,"No sensor is detected in this device",Toast.LENGTH_SHORT).show()
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running){
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            tv_stepsTaken.text = ("$currentSteps")

            progress_circular.applay{
                setProgressWithAnimation(currentSteps.toFloat())
            }
        }

    }

    fun resetSteps(){
        tv_stepsTaken.setOnClickListener{
            Toast.makeText(this,"hold to reset steps", Toast.LENGTH_SHORT).show()
        }
        tv_stepstaken.setOnLongClickListener{
            previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            saveData()

            true

        }
    }

    private fun saveData(){
        val sharedPrefernces = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
        val editor = sharedPrefernces.edit()
        editor.putFloat("key1",previousTotalSteps)
        editor.apply()
    }
    private fun loadData(){
        val sharedPrefernces = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
        val savedNum = sharedPrefernces.getFloat("key1",0f)
        Log.d("MainActivity", "$savedNum")
        previousTotalSteps = savedNum


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}