package com.example.codeinterpretator.blocks

import android.util.Log
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
import com.example.codeinterpretator.createBlock
import com.example.codeinterpretator.interpreter.ExpressionToRPNConverter
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_NAME
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VALUE
import com.example.codeinterpretator.ui.theme.BLOCK_HEIGHT
import com.example.codeinterpretator.ui.theme.BORDER
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.DragTarget
import com.example.codeinterpretator.ui.theme.EQUALSIGN
import com.example.codeinterpretator.ui.theme.SINGLE_WEIGHT
import com.example.codeinterpretator.ui.theme.TYPENAME_BOOL
import com.example.codeinterpretator.ui.theme.TYPENAME_CHAR
import com.example.codeinterpretator.ui.theme.TYPENAME_DOUBLE
import com.example.codeinterpretator.ui.theme.TYPENAME_INT
import com.example.codeinterpretator.ui.theme.TYPENAME_STRING
import com.example.codeinterpretator.ui.theme.TYPE_LABEL_WIDTH
import com.example.codeinterpretator.ui.theme.White

class DeclarationOrAssignmentBlock : Block() {
    var unusableNames: Array<String> =
        arrayOf("null", "if", "for", "fun", "Int", "String", "Bool", "Double", "Float", "Char")
    var variableNames: String =
        "" // Здесь мы берём названия переменных из соответствующего поля блока декларации
    var variableType: String =
        TYPENAME_INT  // Здесь мы берём тип переменной из соответствующего поля блока декларации
    var value: String = ""
    val typesExamples =
        hashMapOf<String, Any>(
            "Int" to 0,
            "String" to "a",
            "Bool" to true,
            "Double" to 0.5,
            "Char" to 'c'
        )


    private fun isRedeclared(variables: HashMap<String, Any>, variableName: String): Boolean {
        if (variables.containsKey(variableName)) {
            return true
        }
        val existingTypes = arrayOf("Int", "String", "Bool", "Double", "Char")
        for (type in existingTypes) {
            if (variables.containsKey(variableName + ":" + type)) {
                return true
            }
        }
        return false
    }

    override public fun translateToRPN(): ArrayList<String> {
        val converter = ExpressionToRPNConverter()
        return converter.convertExpressionToRPN(value)
    }

    override public fun execute(variables: HashMap<String, Any>) {
        var allNames = variableNames.replace(" ", "").split(",")
        for (variableName in allNames) {
            if (variableName == "") {
                Console.print("Вы никак не назвали переменную!")
            } else if (isRedeclared(variables, variableName)) {
                Console.print("Редекларация переменной невозможна")
            } else if (unusableNames.contains(variableName)) {
                Console.print("Пожалуйста, не используйте ключевые слова в качестве названий переменных")
            } else if (!variableName.matches(Regex("[a-zA-Z_][a-zA-Z0-9_]*"))) {
                Console.print("Название переменной содержит запрещённые символы!")
            } else {
                if (value == "") {
                    var fullName = variableName + ":" + variableType
                    variables.put(fullName, 0)
                } else {
                    if (typesExamples[variableType]!!::class == interpretRPN(
                            variables,
                            this.translateToRPN()
                        )::class
                    ) {
                        variables.put(variableName, interpretRPN(variables, this.translateToRPN()))
                    } else {
                        Console.print(
                            "Попытка присвоения переменной типа " + variableType + " значения типа " + interpretRPN(
                                variables,
                                this.translateToRPN()
                            )::class
                        )
                    }
                }
            }
        }

        nextBlock?.execute(variables)
        if (nextBlock == null)
            parentBlock?.executeAfterNesting(variables)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeclarationOrAssignmentBlockView(block: DeclarationOrAssignmentBlock) {
    var variableName by remember { mutableStateOf("") }
    var variableValue by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    variableName = block.variableNames
    variableValue = block.value

    Row(
        modifier = Modifier
            .padding(start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(BorderStroke(BORDER.dp, Black))
            .background(color = White)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(TYPE_LABEL_WIDTH.dp)
                .height(BLOCK_HEIGHT.dp)
        ) {
            TextButton(
                onClick = { dropdownExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(block.variableType)
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_INT
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_INT) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_STRING
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_STRING) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_BOOL
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_BOOL) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_DOUBLE
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_DOUBLE) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_CHAR
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_CHAR) }
                )
            }
        }

        TextField(
            value = variableName,
            onValueChange = {
                variableName = it
                block.variableNames = variableName
            },
            modifier = Modifier
                .weight(SINGLE_WEIGHT)
                .height(BLOCK_HEIGHT.dp),
            label = { Text(BLOCKLABEL_NAME) }
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