package com.example.codeinterpretator.blocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeinterpretator.interpreter.ExpressionToRPNConverter
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.screens.Workspace
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_EXPRESSION
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_IF
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_WHILE
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.ERROR_NOT_BOOLEAN_TYPE
import com.example.codeinterpretator.ui.theme.SMALL_FONT_SIZE
import com.example.codeinterpretator.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WhileBlock : NestingBlock() {
    var expression: String =
        "" // Здесь мы берём выражение переменной из соответствующего поля блока

    override public fun translateToRPN(): ArrayList<String> {
        val converter = ExpressionToRPNConverter()
        return converter.convertExpressionToRPN(expression)
    }

    override public fun execute(variables: HashMap<String, Any>) {
        var result = interpretRPN(variables, this.translateToRPN())
        val variablesToRemove = arrayListOf<String>()
        var currentVariablesSize = variables.size
        while(true) {
            if (result is Boolean == false) {
                Console.print(ERROR_NOT_BOOLEAN_TYPE)
                break
            }
            else if (result == false) {
                break
            }
            else {
                /*for (i: Block in ifCorrect) {
                    i.execute(variables)
                    if (variables.size > currentVariablesSize) {
                        variablesToRemove.add(variables.entries.last().key)
                        currentVariablesSize = variables.size
                    }
                }*/

                if (ifCorrect.size != 0) {
                    ifCorrect[0].execute(variables)
                }
                result = interpretRPN(variables, this.translateToRPN())
            }
        }
        /*for (key in variablesToRemove) {
            variables.remove(key)
        }*/
    }
}

@Composable
fun WhileBlockView(block: WhileBlock, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    if (block.isPreview) {
        WhilePart(block)
    } else {
        Column() {
            WhilePart(block)

            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                            block.nestedBlocksCount += 1
                            Workspace.parentBlock = block
                            Workspace.additionalList = block.ifCorrect
                            Workspace.addBlockAt =
                                blockList.indexOf(block) + block.nestedBlocksCount
                        }
                    }, modifier = Modifier.padding(end = BETWEEN_BLOCK_DISTANCE.dp)
                ) {
                    Text("+", fontSize = SMALL_FONT_SIZE.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhilePart(block: WhileBlock) {
    var expression by remember { mutableStateOf("") }
    expression = block.expression

    Row(
        modifier = Modifier
            .padding(
                start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp
            )
            .border(BorderStroke(2.dp, Black))
            .background(color = White)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text(BLOCKTEXT_WHILE)
        }

        TextField(value = expression, onValueChange = {
            expression = it
            block.expression = expression
        }, modifier = Modifier
            .weight(1f)
            .height(60.dp), label = { Text(BLOCKLABEL_EXPRESSION) })
    }
}