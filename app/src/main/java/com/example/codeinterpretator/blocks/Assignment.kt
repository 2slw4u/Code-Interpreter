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
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VALUE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VARIABLE
import com.example.codeinterpretator.ui.theme.EQUALSIGN

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

    override public fun execute(variables: HashMap<String, Any>) {
        if (value == "") {
            println("Попытка присвоения переменной пустого выражения")
        }
        if (variables.containsKey(variableName) && variables[variableName] is Array<*>) {
            if (variables.containsKey(value) && variables[value] is Array<*>) {
                var firstArray = variables[variableName] as Array<Any>
                val secondArray = variables[value] as Array<Any>
                if (getType(firstArray[0]) != getType(secondArray[0])) {
                    println("Нельзя присваивать массивы разных типов!")
                } else {
                    for (i in secondArray.indices) {
                        if (i < firstArray.size) {
                            firstArray[i] = secondArray[i]
                        } else {
                            println(
                                "Размер массива " +
                                        value +
                                        " больше, чем размер массива " +
                                        variableName +
                                        ". Было присвоено только " +
                                        (i - 1).toString() +
                                        " элементов."
                            )
                            break
                        }
                    }
                    if (secondArray.size < firstArray.size) {
                        println(
                            "Размер массива " +
                                    value +
                                    " меньше, чем размер массива " +
                                    variableName +
                                    ". Было присвоено только " +
                                    (secondArray.size).toString() +
                                    " элементов."
                        )
                    }
                    variables.put(variableName, firstArray)
                }
            } else {
                println("Полное присваивание массива возможно только на этапе объявления!")
            }
        } else if (variableName.matches(Regex("""\w+\[\s*.*?\s*]"""))) {
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
                array[index] = interpretRPN(variables, this.translateToRPN())
                variables.put(arrayName, array)
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
        } else {
            val result = interpretRPN(variables, this.translateToRPN())
            if (variables.containsKey(variableName)) {
                if (variables[variableName]!!::class == result::class) {
                    variables.put(variableName, result)
                } else {
                    println(
                        "Попытка присвоения переменной типа " +
                                getType(variables[variableName]!!) +
                                " значение типа " +
                                getType(result)
                    )
                }
            } else {
                var variableIsDeclared: Boolean = false
                for (type: String in variableTypes) {
                    if (variables.containsKey(variableName + ":" + type)) {
                        if (typesExamples[type]!!::class == result::class) {
                            variables.put(variableName, result)
                            variables.remove(variableName + ":" + type)
                        } else {
                            println(
                                "Попытка присвоения переменной типа " +
                                        type +
                                        " значение типа " +
                                        getType(result)
                            )
                        }
                        variableIsDeclared = true
                        break
                    }
                }
                if (!variableIsDeclared) {
                    println("Попытка присвоения значений необъявленной переменной: " + variableName)
                }
            }
        }

        nextBlock?.execute(variables)
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
            .padding(start = 10.dp, end = 10.dp)
            .border(BorderStroke(2.dp, Color.Black))
            .background(color = Color.White)
    ) {
        TextField(
            value = variableName,
            onValueChange = {
                variableName = it
                block.variableName = variableName
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            label = { Text(BLOCKLABEL_VARIABLE) }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
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
                .weight(1f)
                .height(60.dp),
            label = { Text(BLOCKLABEL_VALUE) }
        )
    }
}