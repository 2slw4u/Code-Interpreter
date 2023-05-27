package com.example.codeinterpretator.interpreter

fun getType(value: Any): String {
    if (value is Int) {
        return "Int"
    }
    else if (value is String) {
        return "String"
    }
    else if (value is Boolean) {
        return "Boolean"
    }
    else if (value is Double) {
        return "Double"
    }
    else if (value is Char) {
        return "Char"
    }
    return "ты че"
}

fun convertToIntOrNull(value: String): Int? {
    return if (value.toDoubleOrNull()?.rem(1) == 0.0) {
        value.toDouble().toInt()
    } else {
        null
    }
}

fun convertToCharIf(value: Int): Any {
    if (value in Char.MIN_VALUE.code..Char.MAX_VALUE.code) {
        return value.toChar()
    } else {
        println("Значение выражения вышло за границы Char и было конвертировано в Int.")
        return value
    }
}

fun extractIndexSubstring(expression: String, startSymbol: Char, endSymbol: Char): String {
    var inBracket: Int = 0
    var result: StringBuilder = StringBuilder()
    for (char: Char in expression) {
        if (char == startSymbol) {
            if (inBracket > 0) {
                result.append(startSymbol)
            }
            inBracket += 1
        } else if (char == endSymbol && inBracket > 0) {
            inBracket -= 1
            if (inBracket > 0) {
                result.append(endSymbol)
            }
        } else {
            if (inBracket > 0) {
                result.append(char)
            }
        }
    }
    return result.toString()
}