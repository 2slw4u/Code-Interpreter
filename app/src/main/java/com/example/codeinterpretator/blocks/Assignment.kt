package com.example.codeinterpretator.blocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.codeinterpretator.interpreter.getType
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VALUE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VARIABLE
import com.example.codeinterpretator.ui.theme.BLOCK_HEIGHT
import com.example.codeinterpretator.ui.theme.BORDER
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.EQUALSIGN
import com.example.codeinterpretator.ui.theme.SINGLE_WEIGHT
import com.example.codeinterpretator.ui.theme.White
import com.example.codeinterpretator.ui.theme.*

class AssignmentBlock : Block() {
    var variableName: String =
        "" // Здесь мы берём название переменной из соответствующего поля блока присваивания
    var value: String = "" // Здесь мы берём присваиваемое значение из соответствующего поля
    val variableTypes = arrayOf("Int", "String", "Bool", "Double", "Char")
    val typesExamples =
        hashMapOf<String, Any>(
            "Int" to 0,
            "String" to "a",
            "Bool" to true,
            "Double" to 0.5,
            "Char" to 'c'
        )

    override public fun translateToRPN(): ArrayList<String> {
        val converter = ExpressionToRPNConverter()
        return converter.convertExpressionToRPN(value)
    }

    private fun workingWithArray(variables: HashMap<String, Any>) {
        if (variables.containsKey(value) && variables[value] is Array<*>) {
            val firstArray = variables[variableName] as Array<Any>
            val secondArray = variables[value] as Array<Any>
            if (getType(firstArray[0]) != getType(secondArray[0])) {
                Console.print(ERROR_DIFFERENT_TYPES_ARRAY_ASSIGNMENT)
            } else {
                for (i in secondArray.indices) {
                    if (i < firstArray.size) {
                        firstArray[i] = secondArray[i]
                    } else {
                        Console.print(ERROR_DIFFERENT_ARRAY_SIZES + (i - 1).toString())
                        break
                    }
                }
                if (secondArray.size < firstArray.size) {
                    Console.print(ERROR_DIFFERENT_ARRAY_SIZES + (secondArray.size).toString())
                }
                variables.put(variableName, firstArray)
            }
        } else {
            Console.print(ERROR_STOP_FULL_ARRAY_ASSIGNMENT)
        }
    }

    private fun workingWithElementOfArray(variables: HashMap<String, Any>) {
        val arrayName = variableName.substringBefore("[")
        val indexString = extractIndexSubstring(variableName, '[', ']')
        val converter = ExpressionToRPNConverter()
        val index =
            convertToIntOrNull(
                interpretRPN(
                    variables,
                    converter.convertExpressionToRPN(indexString.toString())
                )
                    .toString()
            )
        val array = variables[arrayName] as? Array<Any>
        if (array != null && index != null && index >= 0 && index < array.size) {
            val processedValue = interpretRPN(variables, this.translateToRPN())
            if (array[index]::class == processedValue::class) {
                array[index] = processedValue
                variables.put(arrayName, array)
            }
            else {
                Console.print(ERROR_ARRAY_UNCOMPATIBLE_TYPES+ array[index]::class + "; " + processedValue::class)
            }
        } else {
            if (array == null) {
                Console.print(
                    ERROR_INCORRECT_ARRAY_NAME + variableName
                )
            } else {
                Console.print(
                    ERROR_INCORRECT_ARRAY_NAME + variableName + ", " + index
                )
            }
        }
    }

    private fun workingWithBasicTypes(variables: HashMap<String, Any>) {
        val result = interpretRPN(variables, this.translateToRPN())
        if (variables.containsKey(variableName)) {
            if (variables[variableName]!!::class == result::class) {
                variables.put(variableName, result)
            } else {
                Console.print(ERROR_DIFFERENT_TYPES_VARIABLES_ASSIGNMENT +getType(variables[variableName]!!) +" ; " + getType(result))
            }
        } else {
            var variableIsDeclared: Boolean = false
            for (type: String in variableTypes) {
                if (variables.containsKey(variableName + ":" + type)) {
                    if (typesExamples[type]!!::class == result::class) {
                        variables.put(variableName, result)
                        variables.remove(variableName + ":" + type)
                    } else {
                        Console.print(ERROR_DIFFERENT_TYPES_VARIABLES_ASSIGNMENT +getType(result) +" ; " +type)
                    }
                    variableIsDeclared = true
                    break
                }
            }
            if (!variableIsDeclared) {
                Console.print(ERROR_UNDECLARED_VARIABLE_ASSIGNMENT + variableName)
            }
        }
    }

    override public fun execute(variables: HashMap<String, Any>) {
        if (value == "") {
            Console.print(ERROR_ASSIGNING_EMPTY_EXPRESSION)
        }
        if (variables.containsKey(variableName) && variables[variableName] is Array<*>) {
            workingWithArray(variables)
        } else if (variableName.matches(Regex("""\w+\[\s*.*?\s*]"""))) {
            workingWithElementOfArray(variables)
        } else {
            workingWithBasicTypes(variables)
        }

        nextBlock?.execute(variables)
        if (nextBlock == null)
            parentBlock?.executeAfterNesting(variables)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentBlockView(block: AssignmentBlock) {
    var variableName by remember { mutableStateOf("") }
    var variableValue by remember { mutableStateOf("") }

    variableName = block.variableName
    variableValue = block.value

    Row(
        modifier = Modifier
            .padding(start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(BorderStroke(BORDER.dp, Black))
            .background(color = White)
    ) {
        TextField(
            value = variableName,
            onValueChange = {
                variableName = it
                block.variableName = variableName
            },
            modifier = Modifier
                .weight(SINGLE_WEIGHT)
                .height(BLOCK_HEIGHT.dp),
            label = { Text(BLOCKLABEL_VARIABLE) }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(BLOCK_HEIGHT.dp)
                .padding(BETWEEN_BLOCK_DISTANCE.dp)
        ) {
            Text(EQUALSIGN)
        }

        TextField(
            value = variableValue,
            onValueChange = {
                variableValue = it
                block.value = variableValue
            },
            modifier = Modifier
                .weight(SINGLE_WEIGHT)
                .height(BLOCK_HEIGHT.dp),
            label = { Text(BLOCKLABEL_VALUE) }
        )
    }
}