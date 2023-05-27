package com.example.codeinterpretator.blocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.codeinterpretator.interpreter.ExpressionToRPNConverter
import com.example.codeinterpretator.interpreter.convertToIntOrNull
import com.example.codeinterpretator.interpreter.extractIndexSubstring
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_OUTPUT
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VARIABLE
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_PRINT
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_READ
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.White

class InputBlock: Block() {
    var variableName = ""
    var value = "" // Это то, что нам надо вывести
    val typesExamples =
        hashMapOf<String, Any>(
            "Int" to 0,
            "String" to "a",
            "Bool" to true,
            "Double" to 0.5,
            "Char" to 'c'
        )
    var temporaryVariables = HashMap<String, Any>()

    override public fun translateToRPN(): ArrayList<String> {
        val converter = ExpressionToRPNConverter()
        return converter.convertExpressionToRPN(value)
    }

    override public fun execute(variables: HashMap<String, Any>) {
        Console.read(this)
        temporaryVariables = variables
    }

    private fun declaredVariableType(variables: HashMap<String, Any>): String {
        for (type in arrayOf("Int", "Bool", "Double", "Char", "String")) {
            if (variables.contains(variableName + ":" + type)) {
                return type
            }
        }
        return "None"
    }

    public fun continueExecution(variables: HashMap<String, Any>) {
        val processedValue = interpretRPN(variables, this.translateToRPN())
        if (variableName == "") {
            Console.print("Введите имя считываемой переменной")
        }
        else if (variables.contains(variableName)) {
            if (variables[variableName] is Array<*>) {
                Console.print("Нельзя считать весь массив сразу")
            }
            else if (variables[variableName]!!::class == processedValue::class) {
                variables.put(variableName, processedValue)
            }
            else {
                Console.print("Переменной типа " + variables[variableName]!!::class + " присваивается значение типа " + processedValue::class)
            }
        }
        else if (declaredVariableType(variables) != "None") {
            val variableType = declaredVariableType(variables)
            if (typesExamples[variableType]!!::class == processedValue::class) {
                variables.put(variableName, processedValue)
                variables.remove(variableName + ":" + variableType)
            }
            else {
                Console.print("Переменной типа " + typesExamples[variableType]!!::class + " присваивается значение типа " + processedValue::class)
            }
        }
        else if (variableName.matches(Regex("""\w+\[\s*.*?\s*]"""))) {
            val arrayName = variableName.substringBefore("[")
            val indexString = extractIndexSubstring(variableName, '[', ']')
            var converter = ExpressionToRPNConverter()
            val index =
                convertToIntOrNull(
                    interpretRPN(
                        variables,
                        converter.convertExpressionToRPN(indexString.toString())
                    )
                        .toString()
                )
            var array = variables[arrayName] as? Array<Any>
            if (array != null && index != null && index >= 0 && index < array.size) {
                if (array[index]::class == processedValue::class) {
                    array[index] = processedValue
                    variables.put(arrayName, array)
                }
                else {
                    Console.print("В массив типа " + array[index]::class + " нельзя вставить значение типа " + processedValue::class)
                }
            } else {
                if (array == null) {
                    throw IllegalArgumentException(
                        "Указан некорректное название массива для переменной: $variableName"
                    )
                } else {
                    throw IllegalArgumentException(
                        "Указан некорректный индекс для переменной: $variableName, $index"
                    )
                }
            }
        }

        nextBlock?.execute(variables)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputBlockView(block: InputBlock) {
    var argument by remember { mutableStateOf("") }
    var nestedPadding by remember { mutableStateOf(0) }
    nestedPadding = block.nestedPadding()
    argument = block.variableName

    Row(
        modifier = Modifier
            .padding(start = nestedPadding.dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(BorderStroke(2.dp, Black))
            .background(color = White)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text(BLOCKTEXT_READ)
        }

        TextField(
            value = argument,
            onValueChange = {
                argument = it
                block.variableName = argument
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            label = { Text(BLOCKLABEL_VARIABLE) }
        )
    }
}