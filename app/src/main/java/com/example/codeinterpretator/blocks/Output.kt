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

class OutputBlock : Block() {
    var text: String = ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputBlockView(block: OutputBlock) {
    var argument by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    /*variableName = block.variableName
    variableValue = block.value*/

    Row(
        modifier = Modifier.border(BorderStroke(2.dp, Color.Black))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text("print")
        }

        TextField(
            value = argument,
            onValueChange = {
                argument = it
                block.text = argument
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            label = { Text("Output") }
        )
    }
}