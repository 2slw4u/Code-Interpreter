package com.example.codeinterpretator.blocks

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

class DeclarationBlock {
    var type: String = "Integer"
    var name: String = "name"
    var value: String = "value"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeclarationBlockView(block: DeclarationBlock) {
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
                    Text("Integer")
                }
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(onClick = {
                    variableType = "Integer"
                    block.type = variableType
                    dropdownExpanded = false
                },
                    text = { Text("Integer") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "String"
                    block.type = variableType
                    dropdownExpanded = false
                },
                    text = { Text("String") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "Boolean"
                    block.type = variableType
                    dropdownExpanded = false
                },
                    text = { Text("Boolean") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "Double"
                    block.type = variableType
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
                block.name = variableName
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