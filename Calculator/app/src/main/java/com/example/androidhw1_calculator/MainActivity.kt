package com.example.androidhw1_calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val EXPR = "EXPR"
    private val RESULT = "RESULT"
    private val SAVED_RESULT = "SAVED_RES"
    private val LAST_OPERATOR = "LAST_OP"
    private val IS_DOUBLE = "IS_DOUBLE"
    private var currentExpression: String = ""
    private var savedResult: Double? = null
    private var lastOperator: String = ""
    private var numberIsDouble = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXPR, currentExpression)
        outState.putString(LAST_OPERATOR, lastOperator)
        outState.putBoolean(IS_DOUBLE, numberIsDouble)
        outState.putString(RESULT, resultView.text.toString())
        if (savedResult != null) {
            outState.putDouble(SAVED_RESULT, savedResult!!)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentExpression = savedInstanceState.getString(EXPR)!!
        expressionView.text = currentExpression
        resultView.text = savedInstanceState.getString(RESULT)
        lastOperator = savedInstanceState.getString(LAST_OPERATOR)!!

        savedResult = savedInstanceState.getDouble(SAVED_RESULT)

        numberIsDouble = savedInstanceState.getBoolean(IS_DOUBLE)
    }

    fun onNumberClick(v: View) {
        val button: Button = v as Button
        currentExpression += button.text
        expressionView.text = currentExpression
        resultView.text = ""
        lastOperator = ""
    }

    @SuppressLint("SetTextI18n")
    fun onOperationClick(v: View) {
        val textView: TextView = v as TextView

        if (numberIsDouble && textView.text != "." && lastOperator != "") {
            currentExpression += "0"
        }
        when (textView.id) {
            eq_button.id -> {
                resultView.text = RPNcals().calculateExpression(currentExpression).toString()
                currentExpression = ""
                expressionView.text = currentExpression
            }
            save_button.id -> {
                if (savedResult != null || resultView.text != "") {
                    savedResult = resultView.text.toString().toDouble()
                    resultView.text = savedResult.toString() + getString(R.string._was_saved)
                } else {
                    resultView.text = getString(R.string.No_save)
                }
            }
            load_button.id -> {
                if (savedResult != null) {
                    currentExpression += savedResult.toString()
                    expressionView.text = currentExpression
                } else {
                    resultView.text = getString(R.string.No_load)
                }
            }
            del_button.id -> {
                if (currentExpression != "") {
                    currentExpression = currentExpression.dropLast(1)
                    expressionView.text = currentExpression
                    resultView.text = ""
                }
            }
            plus_button.id, mul_button.id, div_button.id -> {
                while (currentExpression.isNotEmpty()
                    && currentExpression.last() != '('
                    && currentExpression.last() != ')'
                    && currentExpression.last() != '.'
                    && !currentExpression.last().isDigit()
                ) {
                    currentExpression = currentExpression.dropLast(1)
                }
                currentExpression += textView.text
                expressionView.text = currentExpression
                resultView.text = ""
            }
            minus_button.id -> if (lastOperator != getString(R.string.minus)) {
                currentExpression += textView.text
                expressionView.text = currentExpression
                resultView.text = ""
            }
            dot_button.id -> {
                if (currentExpression.isNotEmpty() && currentExpression.last().isDigit()) {
                    if (!numberIsDouble) {
                        numberIsDouble = true
                        currentExpression += textView.text
                        expressionView.text = currentExpression
                    }
                }
                resultView.text = ""
            }
            else -> {
                currentExpression += textView.text
                expressionView.text = currentExpression
                resultView.text = ""
            }
        }

        if (textView.id != dot_button.id) {
            numberIsDouble = false
        }
        lastOperator = textView.text.toString()
    }

}
