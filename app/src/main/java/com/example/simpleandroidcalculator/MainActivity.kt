package com.example.simpleandroidcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.math.ln
import kotlin.math.log10
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function

class MainActivity : AppCompatActivity() {
    private lateinit var expressionView: TextView
    private lateinit var resultView: TextView

    private val expression = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expressionView = findViewById(R.id.expressionView)
        resultView = findViewById(R.id.resultView)

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

        bindToken(R.id.btnDot, ".")
        bindToken(R.id.btnAdd, "+")
        bindToken(R.id.btnSubtract, "-")
        bindToken(R.id.btnMultiply, "*")
        bindToken(R.id.btnDivide, "/")
        bindToken(R.id.btnPower, "^")
        bindToken(R.id.btnLeftParen, "(")
        bindToken(R.id.btnRightParen, ")")
        bindToken(R.id.btnPi, "pi")
        bindToken(R.id.btnE, "e")

        findViewById<Button>(R.id.btnSin).setOnClickListener { appendFunction("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { appendFunction("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { appendFunction("tan") }
        findViewById<Button>(R.id.btnLn).setOnClickListener { appendFunction("ln") }
        findViewById<Button>(R.id.btnLog).setOnClickListener { appendFunction("log") }
        findViewById<Button>(R.id.btnSqrt).setOnClickListener { appendFunction("sqrt") }

        findViewById<Button>(R.id.btnPercent).setOnClickListener { appendPercent() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { deleteLast() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnEqual).setOnClickListener { evaluateExpression(showFinal = true) }
    }

    private fun bindDigit(buttonId: Int, digit: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            appendToken(digit)
        }
    }

    private fun bindToken(buttonId: Int, token: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            appendToken(token)
        }
    }

    private fun appendFunction(name: String) {
        appendToken("$name(")
    }

    private fun appendPercent() {
        appendToken("/100")
    }

    private fun appendToken(token: String) {
        expression.append(token)
        refreshDisplay()
        evaluateExpression(showFinal = false)
    }

    private fun deleteLast() {
        if (expression.isNotEmpty()) {
            expression.deleteCharAt(expression.lastIndex)
            refreshDisplay()
            if (expression.isEmpty()) {
                resultView.text = "0"
            } else {
                evaluateExpression(showFinal = false)
            }
        }
    }

    private fun clearAll() {
        expression.clear()
        expressionView.text = ""
        resultView.text = "0"
    }

    private fun refreshDisplay() {
        expressionView.text = expression.toString()
    }

    private fun evaluateExpression(showFinal: Boolean) {
        if (expression.isEmpty()) {
            resultView.text = "0"
            return
        }

        val lnFunction = Function("ln", 1) {
            ln(it[0])
        }
        val logFunction = Function("log", 1) {
            log10(it[0])
        }

        try {
            val value = ExpressionBuilder(expression.toString())
                .functions(lnFunction, logFunction)
                .build()
                .evaluate()

            val formatted = formatNumber(value)
            resultView.text = formatted
            if (showFinal) {
                if (formatted == "Error") return
                expression.clear()
                expression.append(formatted)
                refreshDisplay()
            }
        } catch (_: Exception) {
            if (showFinal) {
                resultView.text = "Error"
            }
        }
    }

    private fun formatNumber(number: Double): String {
        if (!number.isFinite()) return "Error"
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            String.format(Locale.US, "%.10f", number).trimEnd('0').trimEnd('.')
        }
    }
}
