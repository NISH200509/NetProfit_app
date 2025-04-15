package com.example.calculator

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val revenueInput = findViewById<EditText>(R.id.revenueInput)
        val expensesInput = findViewById<EditText>(R.id.expensesInput)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val netProfitResult = findViewById<TextView>(R.id.netProfitResult)
        val resultContainer = findViewById<View>(R.id.resultContainer)
        val bonusMessage = findViewById<TextView>(R.id.bonusMessage)
        val revenueBar = findViewById<View>(R.id.revenueBar)
        val expensesBar = findViewById<View>(R.id.expensesBar)
        val helpButton = findViewById<ImageButton>(R.id.helpButton)

        // Button glow effect
        calculateButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).start()
                    v.animate().alpha(0.8f).setDuration(100).start()
                }
                android.view.MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    v.animate().alpha(1f).setDuration(100).start()
                }
            }
            false
        }

        // Help dialog
        helpButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("How to Use")
                .setMessage("1. Enter Revenue\n2. Enter Expenses\n3. Tap the button to calculate Net Profit.")
                .setPositiveButton("OK", null)
                .show()
        }

        calculateButton.setOnClickListener {
            val revenue = revenueInput.text.toString().toDoubleOrNull() ?: 0.0
            val expenses = expensesInput.text.toString().toDoubleOrNull() ?: 0.0
            val netProfit = revenue - expenses

            // Format result with color
            val profitText = "Net Profit: $${"%.2f".format(netProfit)}"
            netProfitResult.text = profitText

            if (netProfit >= 0) {
                netProfitResult.setTextColor(ContextCompat.getColor(this, R.color.profit_green))
                bonusMessage.text = "Great job! 🎊 Want tips to grow further?"
            } else {
                netProfitResult.setTextColor(ContextCompat.getColor(this, R.color.loss_red))
                bonusMessage.text = "Let's optimize! 💡 Try reducing expenses."
            }

            // Update mini visualization
            val total = revenue + expenses
            if (total > 0) {
                val revenuePercent = (revenue / total * 100).toInt()
                val expensesPercent = (expenses / total * 100).toInt()

                val layoutParamsRev = revenueBar.layoutParams
                layoutParamsRev.height = revenuePercent * 60 / 100
                revenueBar.layoutParams = layoutParamsRev

                val layoutParamsExp = expensesBar.layoutParams
                layoutParamsExp.height = expensesPercent * 60 / 100
                expensesBar.layoutParams = layoutParamsExp
            }

            // Show result with animation
            resultContainer.visibility = View.VISIBLE
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = 500
            resultContainer.startAnimation(fadeIn)
        }
    }
}