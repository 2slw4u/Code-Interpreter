package com.example.codeinterpretator.interpreter

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
                        throw IllegalArgumentException("Указан некорректное название массива для переменной: $token")
                    } else {
                        throw IllegalArgumentException("Указан некорректный индекс для переменной: $token, $index")
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
                    throw IllegalArgumentException("Переменная $token имеет значение null!")
                }
            }
            else -> {
                if (stack.size < 2) {
                    throw IllegalArgumentException("Выражение не имеет достаточно переменных!")
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
                            throw IllegalArgumentException(
                                "Невозможная операция: Int $token Char. Может быть, вы имели в виду, Char $token Int?"
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() + operand2.toString().toInt()
                        } else if (operand1 is Int || operand2 is Int || operand1 is Double && operand2 is Double) {
                            operand1.toString().toDouble() + operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException(
                                "Невозможная операция: Сложение нечисловых переменных."
                            )
                        }
                    }
                    "-" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Вычитание нечисловых переменных."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code - operand2.code
                        }  else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code - operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Int $token Char. Может быть, вы имели в виду, Char $token Int?"
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() - operand2.toString().toInt()
                        } else if (operand1 is Int || operand2 is Int || operand1 is Double && operand2 is Double) {
                            operand1.toString().toDouble() - operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException(
                                "Невозможная операция: Вычитание нечисловых переменных."
                            )
                        }
                    }
                    "*" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Умножение нечисловых переменных."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code * operand2.code
                        }  else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code * operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Int $token Char. Может быть, вы имели в виду, Char $token Int?"
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() * operand2.toString().toInt()
                        } else if (operand1 is Int || operand2 is Int || operand1 is Double && operand2 is Double) {
                            operand1.toString().toDouble() * operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException(
                                "Невозможная операция: Умножение нечисловых переменных."
                            )
                        }
                    }
                    "/" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Деление нечисловых переменных."
                            )
                        } else if (!(operand2 is Char) && operand2.toString().toDouble() == 0.0) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Деление на 0."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code / operand2.code
                        } else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code / operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Int $token Char. Может быть, вы имели в виду, Char $token Int?"
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() / operand2.toString().toInt()
                        } else if (operand1 is Int || operand2 is Int || operand1 is Double && operand2 is Double) {
                            operand1.toString().toDouble() / operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException(
                                "Невозможная операция: Деление нечисловых переменных."
                            )
                        }
                    }
                    "%" -> {

                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Взятие % для нечисловых переменных."
                            )
                        } else if (!(operand2 is Char) && operand2.toString().toDouble() == 0.0) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Взятие % 0."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code % operand2.code
                        } else if (operand1 is Char && operand2 is Int) {
                            convertToCharIf(operand1.code % operand2)
                        } else if (operand1 is Int && operand2 is Char) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Int $token Char. Может быть, вы имели в виду, Char $token Int?"
                            )
                        } else if (operand1 is Int && operand2 is Int){
                            operand1.toString().toInt() % operand2.toString().toInt()
                        } else if (operand1 is Int || operand2 is Int || operand1 is Double && operand2 is Double) {
                            operand1.toString().toDouble() % operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException(
                                "Невозможная операция: Взятие % для нечисловых переменных."
                            )
                        }
                    }
                    ">" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Сравнение нечисловых переменных."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code > operand2.code
                        } else if (operand1::class == operand2::class) {
                            operand1.toString().toDouble() > operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException("Сравнение переменных разного типа невозможно!")
                        }
                    }
                    ">=" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Сравнение нечисловых переменных."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code >= operand2.code
                        } else if (operand1::class == operand2::class) {
                            operand1.toString().toDouble() >= operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException(
                                "Сравнение переменных разного типа невозможно!"
                            )
                        }
                    }
                    "<" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Сравнение нечисловых переменных."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code < operand2.code
                        } else if (operand1::class == operand2::class) {
                            operand1.toString().toDouble() < operand2.toString().toDouble()
                        } else {
                            throw IllegalArgumentException(
                                "Сравнение переменных разного типа невозможно!"
                            )
                        }
                    }
                    "<=" -> {
                        if (operand1 is String ||
                            operand2 is String ||
                            operand1 is Boolean ||
                            operand2 is Boolean
                        ) {
                            throw IllegalArgumentException(
                                "Невозможная операция: Сравнение нечисловых переменных."
                            )
                        } else if (operand1 is Char && operand2 is Char) {
                            operand1.code <= operand2.code
                        } else if (operand1::class == operand2::class) {
                            //operand1. <= operand2.
                            'l'
                        } else {
                            throw IllegalArgumentException(
                                "Сравнение переменных разного типа невозможно!"
                            )
                        }
                    }
                    "==" -> {
                        if (operand1::class == operand2::class) {
                            operand1 == operand2
                        } else {
                            throw IllegalArgumentException(
                                "Сравнение переменных разного типа невозможно!"
                            )
                        }
                    }
                    "!=" -> {
                        if (operand1::class == operand2::class) {
                            operand1 != operand2
                        } else {
                            throw IllegalArgumentException(
                                "Сравнение переменных разного типа невозможно!"
                            )
                        }
                    }
                    else -> throw IllegalArgumentException(
                        "Оператор инвалид: $token"
                    )
                }
                stack.add(result)
            }
        }
    }
    val resultVal = stack.removeAt(stack.size - 1)
    if (stack.isNotEmpty()) {
        throw IllegalArgumentException("Выражение не имеет достаточно операторов!")
    }
    return resultVal
}