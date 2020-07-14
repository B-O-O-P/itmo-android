package com.example.androidhw1_calculator

import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RPNcals(
    private val operators: String = "+-*/",
    private val delimetets: String = "()$operators",
    private var errorFlag: Boolean = false
) {
    private fun isDelimeter(token: String): Boolean {
        if (token.length != 1) {
            return false
        } else {
            for (i in 0 until delimetets.length) {
                if (token[0] == delimetets[i]) {
                    return true
                }
            }
            return false
        }
    }

    private fun isOperator(token: String): Boolean {
        if (token == "u-") {
            return true
        } else {
            for (i in 0 until operators.length) {
                if (token[0] == operators[i]) {
                    return true
                }
            }
            return false
        }
    }

    private fun getPriority(token: String): Int {
        return when (token) {
            "(" -> 1
            "+", "-" -> 2
            "*", "/" -> 3
            else -> 4
        }
    }

    private fun parseExpression(infix: String): ArrayList<String> {
        val rpn = ArrayList<String>()
        val stack = ArrayDeque<String>()
        val tokenizer = StringTokenizer(infix, delimetets, true)
        var previous = ""
        var current: String
        while (tokenizer.hasMoreTokens()) {
            current = tokenizer.nextToken()
            if (!tokenizer.hasMoreTokens() && isOperator(current)) {
                errorFlag = true
                return rpn
            }

            if (current == " ") {
                continue
            }

            if (isDelimeter(current)) {
                if (current == "(") {
                    stack.push(current)
                } else if (current == ")") {
                    while (stack.peek() != "(") {
                        rpn.add(stack.pop())

                        if (stack.isEmpty()) {
                            errorFlag = true
                            return rpn
                        }
                    }

                    stack.pop()
                    if (!stack.isEmpty()) {
                        rpn.add(stack.pop())
                    }
                } else {
                    if (current == ("-") && previous == "" || (isDelimeter(previous) && previous != ")")) {
                        current = "u-"
                    } else {
                        while (!stack.isEmpty() && (getPriority(current) <= getPriority(
                                stack.peek()
                            ))
                        ) {
                            rpn.add(stack.pop())
                        }
                    }

                    stack.push(current)
                }
            } else {
                rpn.add(current)
            }

            previous = current
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) {
                rpn.add(stack.pop())
            } else {
                errorFlag = true
                return rpn
            }
        }

        return rpn
    }

    private fun calc(rpn: ArrayList<String>): Double? {
        val stack = ArrayDeque<Double>()
        stack.push(0.0)
        for (x in rpn) {
            when (x) {
                "+" -> stack.push(stack.pop() + stack.pop())
                "-" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a - b)
                }
                "*" -> stack.push(stack.pop() * stack.pop())
                "/" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a / b)
                }
                "u-" -> stack.push(-stack.pop())
                else -> stack.push(x.toDouble())
            }
        }

        return stack.pop()
    }


    fun calculateExpression(expr: String): Double? {
        val parsedExpr = parseExpression(expr)
        if (!errorFlag) {
            return calc(parsedExpr)
        }

        return 0.0
    }

}