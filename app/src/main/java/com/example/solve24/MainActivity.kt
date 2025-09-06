package com.example.solve24

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val prefs by lazy { getSharedPreferences("settings", MODE_PRIVATE) }
    private lateinit var limitState: MutableState<Int>
    private lateinit var numbers: SnapshotStateList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            limitState = remember { mutableIntStateOf(prefs.getInt("limit", 10)) }
            numbers = remember { mutableStateListOf(0, 0, 0, 0) }
            var results by remember { mutableStateOf<Map<String, Set<String>>>(emptyMap()) }
            val groupColors = listOf(
                Color(0xFFFFCDD2), Color(0xFFC8E6C9), Color(0xFFBBDEFB),
                Color(0xFFFFF9C4), Color(0xFFD1C4E9), Color(0xFFFFE0B2)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                numbers.forEachIndexed { index, value ->
                    Text(text = "Number ${index + 1}: $value")
                    Slider(
                        value = value.toFloat(),
                        onValueChange = {
                            numbers[index] = it.toInt()
                        },
                        valueRange = 0f..limitState.value.toFloat(),
                        steps = (limitState.value - 1).coerceAtLeast(0)
                    )
                }
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Button(onClick = {
                        results = solve24(numbers.toList())
                    }) { Text("Solve") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    }) { Text("Settings") }
                }
                if (results.isEmpty()) {
                    Text("No solution!", modifier = Modifier.padding(top = 16.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                        var index = 0
                        results.forEach { (_, exprs) ->
                            val color = groupColors[index % groupColors.size]
                            item {
                                Column(modifier = Modifier.fillMaxWidth().background(color).padding(8.dp)) {
                                    exprs.forEach { Text(it) }
                                }
                            }
                            index++
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::limitState.isInitialized) {
            limitState.value = prefs.getInt("limit", 10)
            for (i in 0 until numbers.size) {
                if (numbers[i] > limitState.value) numbers[i] = limitState.value
            }
        }
    }
}
