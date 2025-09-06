package com.example.solve24

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        setContent {
            var limit by remember { mutableIntStateOf(prefs.getInt("limit", 10)) }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Max Number: $limit")
                Slider(
                    value = limit.toFloat(),
                    onValueChange = { limit = it.toInt() },
                    valueRange = 4f..20f,
                    steps = 16
                )
                Button(onClick = {
                    prefs.edit().putInt("limit", limit).apply()
                    finish()
                }) {
                    Text("Save")
                }
            }
        }
    }
}
