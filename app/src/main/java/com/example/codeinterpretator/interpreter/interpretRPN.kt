package com.example.codeinterpretator.interpreter

import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.ui.theme.*

val converter = ExpressionToRPNConverter()
fun interpretRPN(variables: HashMap<String, Any>, RPN: List<String>): Any {
    val stack = ArrayList<Any>()
    for (i in RPN.indices) {
        val token = RPN[i]
        when {
            token.matches(Regex("-?\\d+(\\.\\d+)")) -> {
                stack.add(token.toDouble())
            }
            token.matches(Regex("-?\\d+")) -> {
                stack.add(token.toInt())
            }
            token.matches(Regex("\".*\"")) -> {
                stack.add(token.removeSurrounding("\""))
            }
            token.matches(Regex("'.'")) -> {
                stack.add(token[1])
            }
            token.matches(Regex("""\w+\[\s*.*?\s*]""")) -> {
                val arrayName = token.substringBefore("[")
                val indexString = extractIndexSubstring(token, '[', ']')
                val converter = ExpressionToRPNConverter()
                val index =
                    convertToIntOrNull(
                        interpretRPN(
                            variables,
                            converter.convertExpressionToRPN(indexString.toString())
                        ).toString()
                    )
                val array = variables[arrayName] as? Array<*>
                if (array != null && index != null && index >= 0 && index < array.size) {
                    val arrayElement = array[index]
                    stack.add(arrayElement!!)
                } else {
                    if (array == null) {
                        Console.print(ERROR_INVALID_ARRAY_NAME + token)
                    } else {
                        Console.print(ERROR_INVALID_INDEX + token + ", " + index)
                    }
                }
            }
            token.equals("true", true) || token.equals("false", true) -> {
                stack.add(token.toBoolean())
            }
            variables.containsKey(token) -> {
                if (variables[token] != null) {
                    stack.add(variables[token]!!)
                } else {
                    Console.print(ERROR_NULL_VARIABLE + token )
                }
            }
            else -> {
                if (stack.size < 2) {
                    Console.print(ERROR_INSUFFICIENT_OPERANDS)
                }
                val operand2: Any = stack.removeAt(stack.size - 1)
                val operand1: Any = stack.removeAt(stack.size - 1)
                val result = when (token) {
                    "+" -> {
                        if (operand1 is String && operand2 is String ||
                            operand1 is String && operand2 is Char ||
                            operand1 is Char && operand2 is String
                        ) {
                            operand1.toString() + operand2.toString()
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code + operand2.code
                        }  else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code + operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            Console.print(
                                ERROR_INT_TO_CHAR_INSTEAD_OF_CHAR_TO_INT + token
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() + operand2.toString().toInt()
                        } else if (operand1 is Int || operand2 is Int || operand1 is Double && operand2 is Double) {
                            operand1.toString().toDouble() + operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_ADDITION_NON_NUMERIC
                            )
                        }
                    }
                    "-" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_SUBTRACTION_NON_NUMERIC
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code - operand2.code
                        }  else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code - operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            Console.print(
                                ERROR_INT_TO_CHAR_INSTEAD_OF_CHAR_TO_INT + token
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() - operand2.toString().toInt()
                        } else if ((operand1 is Int || operand2 is Int) && (operand1 is Double || operand2 is Double) || (operand1 is Double && operand2 is Double)) {
                            operand1.toString().toDouble() - operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_SUBTRACTION_NON_NUMERIC
                            )
                        }
                    }
                    "*" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_MULTIPLICATION_NON_NUMERIC
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code * operand2.code
                        }  else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code * operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            Console.print(
                                ERROR_INT_TO_CHAR_INSTEAD_OF_CHAR_TO_INT + token
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() * operand2.toString().toInt()
                        } else if ((operand1 is Int || operand2 is Int) && (operand1 is Double || operand2 is Double) || (operand1 is Double && operand2 is Double)) {
                            operand1.toString().toDouble() * operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_MULTIPLICATION_NON_NUMERIC
                            )
                        }
                    }
                    "/" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_DIVISION_NON_NUMERIC
                            )
                        } else if (!(operand2 is Char) && operand2.toString().toDouble() == 0.0) {
                            Console.print(
                                ERROR_DIVISION_BY_ZERO
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code / operand2.code
                        } else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code / operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            Console.print(
                                ERROR_INT_TO_CHAR_INSTEAD_OF_CHAR_TO_INT + token
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() / operand2.toString().toInt()
                        } else if ((operand1 is Int || operand2 is Int) && (operand1 is Double || operand2 is Double) || (operand1 is Double && operand2 is Double)) {
                            operand1.toString().toDouble() / operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_DIVISION_NON_NUMERIC
                            )
                        }
                    }
                    "%" -> {

                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_MODULUS_NON_NUMERIC
                            )
                        } else if (!(operand2 is Char) && operand2.toString().toDouble() == 0.0) {
                            Console.print(
                                ERROR_MODULUS_BY_ZERO
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code % operand2.code
                        } else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code % operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            Console.print(
                                ERROR_INT_TO_CHAR_INSTEAD_OF_CHAR_TO_INT + token
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() % operand2.toString().toInt()
                        } else if ((operand1 is Int || operand2 is Int) &&
                            (operand1 is Double || operand2 is Double) ||
                            (operand1 is Double && operand2 is Double))
                        {
                            operand1.toString().toDouble() % operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_MODULUS_NON_NUMERIC
                            )
                        }
                    }
                    ">" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_COMPARISON_NON_NUMERIC
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code > operand2.code
                        } else if ((operand1 is Int || operand2 is Int) &&
                            (operand1 is Double || operand2 is Double) ||
                            (operand1 is Int && operand2 is Int) ||
                            (operand1 is Double && operand2 is Double))
                        {
                            operand1.toString().toDouble() > operand2.toString().toDouble()
                        } else {
                            Console.print(ERROR_COMPARISON_DIFFERENT_TYPES)
                        }
                    }
                    ">=" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_COMPARISON_NON_NUMERIC
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code >= operand2.code
                        } else if ((operand1 is Int || operand2 is Int) &&
                            (operand1 is Double || operand2 is Double) ||
                            (operand1 is Int && operand2 is Int) ||
                            (operand1 is Double && operand2 is Double))
                        {
                            operand1.toString().toDouble() >= operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_COMPARISON_DIFFERENT_TYPES
                            )
                        }
                    }
                    "<" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_COMPARISON_NON_NUMERIC
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code < operand2.code
                        } else if ((operand1 is Int || operand2 is Int) &&
                            (operand1 is Double || operand2 is Double) ||
                            (operand1 is Int && operand2 is Int) ||
                            (operand1 is Double && operand2 is Double))
                        {
                            operand1.toString().toDouble() < operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_COMPARISON_DIFFERENT_TYPES
                            )
                        }
                    }
                    "<=" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            Console.print(
                                ERROR_COMPARISON_NON_NUMERIC
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code <= operand2.code
                        } else if ((operand1 is Int || operand2 is Int) &&
                            (operand1 is Double || operand2 is Double) ||
                            (operand1 is Int && operand2 is Int) ||
                            (operand1 is Double && operand2 is Double))
                        {
                            operand1.toString().toDouble() <= operand2.toString().toDouble()
                        } else {
                            Console.print(
                                ERROR_COMPARISON_DIFFERENT_TYPES
                            )
                        }
                    }
                    "==" -> {
                        if (operand1::class == operand2::class) {
                            operand1 == operand2
                        } else {
                            Console.print(
                                ERROR_COMPARISON_DIFFERENT_TYPES
                            )
                        }
                    }
                    "!=" -> {
                        if (operand1::class == operand2::class) {
                            operand1 != operand2
                        } else {
                            Console.print(
                                ERROR_COMPARISON_DIFFERENT_TYPES
                            )
                        }
                    }
                    else -> Console.print(
                        ERROR_INVALID_OPERATOR + token
                    )
                }
                stack.add(result)
            }
        }
    }
    var resultVal: Any = false
    if (stack.size > 0) {
        resultVal = stack.removeAt(stack.size - 1)
    }
    if (stack.isNotEmpty()) {
        println(ERROR_INSUFFICIENT_OPERATORS)
    }
    return resultVal
}