package com.example.codeinterpretator.blocks

import android.util.Log
import androidx.compose.foundation.BorderStroke
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

class DeclarationOrAssignmentBlock : Block() {
    var unusableNames: Array<String> =
        arrayOf("null", "if", "for", "fun", "Int", "String", "Bool", "Double", "Float")
    var variableName: String =
        "a" // Здесь мы берём название переменной из соответствующего поля блока декларации
    var variableType: String =
        "Int" // Здесь мы берём тип переменной из соответствующего поля блока декларации
    // Если это поле пустое, то variableType = null; это значит, что мы не декларируем переменную, а
    // переопределяем
    var value: String = "80" // Здесь мы берём присваиваемое значение из соответствующего поля

//   override public fun translateToRPN(): ArrayList<String> {
//        var converter = ExpressionToRPNConverter()
//        return converter.convertExpressionToRPN(value)
//    }

    override public fun execute(variables: HashMap<String, Any>) {
        if (variableName == null) {
            println("Вы никак не назвали переменную!")
            createBlock()
        } else if (variables.containsKey(variableName) && variableType != null) {
            println("Редекларация переменной невозможна")
            // здесь вместо простого вывода в консоль мы выбрасываем пользователю ошибку
        } else if (unusableNames.contains(variableName)) {
            println("Пожалуйста, не используйте ключевые слова в качестве названий переменных")
            // здесь вместо простого вывода в консоль мы выбрасываем пользователю ошибку
        } else if (!variableName.matches(Regex("[a-zA-Z_][a-zA-Z0-9_]*"))) {
            println("Название переменной содержит запрещённые символы!")
        } else {
//            variables.put(variableName, interpretRPN(variables, this.translateToRPN()))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeclarationBlockView(block: DeclarationOrAssignmentBlock) {
    var variableName by remember { mutableStateOf("") }
    var variableValue by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var variableType by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.border(BorderStroke(2.dp, Color.Black))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .border(BorderStroke(2.dp, Color.Black))) {
            TextButton(
                onClick = { dropdownExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (variableType.isNotEmpty()) {
                    Text(variableType)
                } else {
                    Text("Int")
                }
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(onClick = {
                    variableType = "Int"
                    block.variableType = variableType
                    dropdownExpanded = false
                },
                    text = { Text("Int") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "String"
                    block.variableType = variableType
                    dropdownExpanded = false
                },
                    text = { Text("String") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "Bool"
                    block.variableType = variableType
                    dropdownExpanded = false
                },
                    text = { Text("Bool") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "Double"
                    block.variableType = variableType
                    dropdownExpanded = false
                },
                    text = { Text("Double") }
                )
            }
        }

        TextField(
            value = variableName,
            onValueChange = {
                variableName = it
                block.variableName = variableName
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            label = { Text("Name") }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text("=")
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
            label = { Text("Value") }
        )
    }
}