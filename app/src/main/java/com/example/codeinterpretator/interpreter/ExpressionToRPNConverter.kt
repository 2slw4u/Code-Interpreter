package com.example.codeinterpretator.interpreter

class ExpressionToRPNConverter() {
    private fun getPriority(input: String): Int {
        if (input == "(") {
            return 0
        } else if (input == "+" ||
            input == "-" ||
            input == "<" ||
            input == ">" ||
            input == "!=" ||
            input == "==" ||
            input == "%" ||
            input == ">=" ||
            input == "<="
        ) {
            return 1
        } else if (input == "*" || input == "/") {
            return 2
        }
        return 0
    }

    fun formatPart(input: String): String {
        var operators: Array<Char> = arrayOf('+', '-', '/', '*', '>', '<', '%', '!', '=', '(', ')')
        var output: String = ""
        for (i in input.indices) {
            output += input[i]
            if (i + 1 < input.length &&
                operators.contains(input[i + 1]) &&
                !operators.contains(input[i]) &&
                input[i] != ' '
            ) {
                output += " "
            } else if (i + 1 < input.length &&
                operators.contains(input[i]) &&
                input[i + 1] != ' ' &&
                input[i + 1] != '='
            ) {
                output += " "
            }
        }
        return output
    }

    fun formatExpression(input: String): String {
        var result = ""
        var splitByQuotes = input.split("\"")
        for (i in 0..(splitByQuotes.size - 1)) {
            if (i % 2 == 0) {
                result += formatPart(splitByQuotes[i].replace(" ", ""))
            } else {
                result += "\"" + splitByQuotes[i] + "\""
            }
            if (i != splitByQuotes.size - 1) {
                result += " "
            }
        }
        return result
    }

    public fun convertExpressionToRPN(givenExpression: String?): ArrayList<String> {
        var operators: Array<String> = arrayOf("+", "-", "/", "*", ">", "<", "==", "!=", ">=", "<=")
        var result: ArrayList<String> = arrayListOf<String>()
        if (givenExpression == null || givenExpression.length == 0) {
            return arrayListOf<String>()
        }
        var stack: ArrayList<String> = arrayListOf<String>()
        var expression = tokenizeExpression(formatExpression(givenExpression))
        for (currentElement: String in expression) {
            if (currentElement == "(") {
                stack.add(currentElement)
            } else if (currentElement == ")") {
                while (stack[stack.size - 1] != "(") {
                    result.add(stack.removeLast())
                }
                stack.removeLast()
            } else if (operators.contains(currentElement)) {
                while (!stack.isEmpty() &&
                    getPriority(stack[stack.size - 1]) >= getPriority(currentElement)) {
                    result.add(stack.removeLast())
                }
                stack.add(currentElement)
            } else {
                result.add(currentElement)
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.removeLast())
        }
        return result
    }

    private fun tokenizeExpression(expression: String): List<String> {
        var tokens: MutableList<String> = mutableListOf()
        var currentToken: StringBuilder = StringBuilder()
        var inString: Boolean = false
        var inBracket: Int = 0

        for (char: Char in expression) {
            if (char == '"' && !inString) {
                inString = true
                currentToken.append(char)
            } else if (char == '"' && inString) {
                inString = false
                currentToken.append(char)
                tokens.add(currentToken.toString())
                currentToken.clear()
            } else if (char == '[') {
                currentToken.append('[')
                inBracket += 1
            } else if (char == ']' && inBracket > 0) {
                currentToken.append(']')
                inBracket -= 1
            } else {
                if (inBracket > 0) {
                    currentToken.append(char)
                } else {
                    if (!char.isWhitespace()) {
                        currentToken.append(char)
                    } else if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString())
                        currentToken.clear()
                    }
                }
            }
        }

        if (currentToken.isNotEmpty()) {
            tokens.add(currentToken.toString())
        }

        return tokens
    }
}