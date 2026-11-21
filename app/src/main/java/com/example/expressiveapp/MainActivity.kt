package com.example.expressiveapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expressiveapp.ui.theme.ExpressiveAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpressiveAppTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var first by remember { mutableStateOf<Double?>(null) }
    var op by remember { mutableStateOf<String?>(null) }
    var newInput by remember { mutableStateOf(true) }

    fun applyDigit(d: String) {
        display = if (newInput || display == "0") d else display + d
        newInput = false
    }

    fun applyOp(o: String) {
        first = display.toDoubleOrNull()
        op = o
        newInput = true
    }

    fun calculate() {
        val a = first ?: return
        val b = display.toDoubleOrNull() ?: return
        val result = when (op) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> if (b != 0.0) a / b else Double.NaN
            else -> b
        }
        display = if (result.isNaN()) "Hata" else result.toString().removeSuffix(".0")
        first = null
        op = null
        newInput = true
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Hesap Makinesi") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = display,
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            val rows = listOf(
                listOf("7", "8", "9", "÷"),
                listOf("4", "5", "6", "×"),
                listOf("1", "2", "3", "-"),
                listOf("0", "C", "=", "+")
            )
            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { label ->
                        Button(
                            onClick = {
                                when (label) {
                                    "C" -> { display = "0"; first = null; op = null; newInput = true }
                                    "=" -> calculate()
                                    "+", "-", "×", "÷" -> applyOp(label)
                                    else -> applyDigit(label)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(label, style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
    }
}
