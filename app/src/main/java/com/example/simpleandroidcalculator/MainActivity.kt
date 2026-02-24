package com.example.simpleandroidcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView

    private var firstNumber: Double? = null
    private var pendingOperation: Operation? = null
    private var isTypingNewNumber = true

    private enum class Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        bindDigit(R.id.btn0, "0")
        bindDigit(R.id.btn1, "1")
        bindDigit(R.id.btn2, "2")
        bindDigit(R.id.btn3, "3")
        bindDigit(R.id.btn4, "4")
        bindDigit(R.id.btn5, "5")
        bindDigit(R.id.btn6, "6")
        bindDigit(R.id.btn7, "7")
        bindDigit(R.id.btn8, "8")
        bindDigit(R.id.btn9, "9")

        findViewById<Button>(R.id.btnDot).setOnClickListener { appendDot() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnSign).setOnClickListener { toggleSign() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { percent() }
        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperation(Operation.ADD) }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { setOperation(Operation.SUBTRACT) }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperation(Operation.MULTIPLY) }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperation(Operation.DIVIDE) }
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
    }

    private fun bindDigit(buttonId: Int, digit: String) {
        findViewById<Button>(buttonId).setOnClickListener { appendDigit(digit) }
    }

    private fun clearAll() {
        display.text = "0"
        firstNumber = null
        pendingOperation = null
        isTypingNewNumber = true
    }

    private fun appendDigit(digit: String) {
        val current = display.text.toString()
        display.text = if (isTypingNewNumber || current == "0") {
            isTypingNewNumber = false
            digit
        } else {
            current + digit
        }
    }

    private fun appendDot() {
        val current = display.text.toString()
        if (isTypingNewNumber) {
            display.text = "0."
            isTypingNewNumber = false
            return
        }
        if (!current.contains('.')) {
            display.text = "$current."
        }
    }

    private fun toggleSign() {
        val value = display.text.toString().toDoubleOrNull() ?: return
        display.text = formatNumber(-value)
    }

    private fun percent() {
        val value = display.text.toString().toDoubleOrNull() ?: return
        display.text = formatNumber(value / 100)
    }

    private fun setOperation(operation: Operation) {
        if (firstNumber == null) {
            firstNumber = display.text.toString().toDoubleOrNull()
        } else if (!isTypingNewNumber) {
            calculateResult()
            firstNumber = display.text.toString().toDoubleOrNull()
        }

        pendingOperation = operation
        isTypingNewNumber = true
    }

    private fun calculateResult() {
        val operation = pendingOperation ?: return
        val first = firstNumber ?: return
        val second = display.text.toString().toDoubleOrNull() ?: return

        val result = when (operation) {
            Operation.ADD -> first + second
            Operation.SUBTRACT -> first - second
            Operation.MULTIPLY -> first * second
            Operation.DIVIDE -> {
                if (second == 0.0) {
                    display.text = "Error"
                    firstNumber = null
                    pendingOperation = null
                    isTypingNewNumber = true
                    return
                }
                first / second
            }
        }

        display.text = formatNumber(result)
        firstNumber = null
        pendingOperation = null
        isTypingNewNumber = true
    }

    private fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }
}
