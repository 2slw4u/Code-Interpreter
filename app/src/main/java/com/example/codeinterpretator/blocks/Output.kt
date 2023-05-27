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
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_OUTPUT
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_PRINT
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.DragTarget
import com.example.codeinterpretator.ui.theme.White

class OutputBlock : Block() {
    var value = "" //Это то, что нам надо вывести
    override public fun translateToRPN(): ArrayList<String> {
        var converter = ExpressionToRPNConverter()
        return converter.convertExpressionToRPN(value)
    }

    override public fun execute(variables: HashMap<String, Any>) {
        Console.print(interpretRPN(variables, this.translateToRPN()).toString())
        nextBlock?.execute(variables)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputBlockView(block: OutputBlock) {
    var argument by remember { mutableStateOf("") }

    argument = block.value

    Row(
        modifier = Modifier
            .padding(start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(BorderStroke(2.dp, Black))
            .background(color = White)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text(BLOCKTEXT_PRINT)
        }

        TextField(
            value = argument,
            onValueChange = {
                argument = it
                block.value = argument
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            label = { Text(BLOCKLABEL_OUTPUT) }
        )
    }
}