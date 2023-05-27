package com.example.codeinterpretator.blocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeinterpretator.RenderBlock
import com.example.codeinterpretator.interpreter.ExpressionToRPNConverter
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.screens.Workspace
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_EXPRESSION
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_OUTPUT
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_ELSE
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_IF
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_PRINT
import com.example.codeinterpretator.ui.theme.DragTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IfElseBlock : NestingBlock() {
    var ifCorrect: ArrayList<Block> = arrayListOf<Block>()
    var ifWrong: ArrayList<Block> = arrayListOf<Block>()
    var expression: String =
        "" // Здесь мы берём выражение переменной из соответствующего поля блока
    var isPreview: Boolean = false
    var nestedBlocksCount = 0

    override public fun translateToRPN(): ArrayList<String> {
        val converter = ExpressionToRPNConverter()
        return converter.convertExpressionToRPN(expression)
    }

    override public fun execute(variables: HashMap<String, Any>) {
        if (interpretRPN(variables, this.translateToRPN()) == 1) {
            if (ifCorrect.size != 0)
                ifCorrect[0].execute(variables)
            else
                executeAfterNesting(variables)
        } else {
            if (ifWrong.size != 0)
                ifWrong[0].execute(variables)
            else
                executeAfterNesting(variables)
        }
    }

    public fun addBlockToCorrect(new: Block) {
        ifCorrect.add(new)
    }

    public fun addBlockToWrong(new: Block) {
        ifWrong.add(new)
    }
}
val list = mutableStateListOf<Block>(OutputBlock())
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IfElseBlockView(block: IfElseBlock, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    if (block.isPreview) {
        IfPart(block)
    }
    else {
        Column() {
            IfPart(block)

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd) {
                Button(
                    onClick = { scope.launch {
                        scaffoldState.drawerState.open()
                        block.nestedBlocksCount += 1
                        Workspace.parentBlock = block
                        Workspace.additionalList = block.ifCorrect
                        Workspace.addBlockAt = blockList.indexOf(block) + block.nestedBlocksCount
                    } },
                    modifier = Modifier
                        .padding(end = BETWEEN_BLOCK_DISTANCE.dp)
                ) {
                    Text("+", fontSize = 10.sp)
                }
            }

            /*LazyColumn(modifier = Modifier.padding(start = tab.dp)) {

            }*/
            /*LazyColumn(
                modifier = Modifier.padding(start = tab.dp)
            ) {
                item {Text("hi")}
                item {Text("hi")}
            }*/
            /*LazyColumn(
                //modifier = Modifier.padding(start = 20.dp)
            ) {
                itemsIndexed(list) { index, item ->
                    Text("hi")
                }
            }*/
            //ElsePart(block)
            /*NestedBlockColumn(block.ifWrong)*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IfPart(block: IfElseBlock) {
    var expression by remember { mutableStateOf("") }
    expression = block.expression

    Row(
        modifier = Modifier
            .padding(start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(BorderStroke(2.dp, Color.Black))
            .background(color = Color.White)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text(BLOCKTEXT_IF)
        }

        TextField(
            value = expression,
            onValueChange = {
                expression = it
                block.expression = expression
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            label = { Text(BLOCKLABEL_EXPRESSION) }
        )
    }
}

@Composable
fun ElsePart(block: IfElseBlock) {
    Row(
        modifier = Modifier
            .padding(start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(BorderStroke(2.dp, Color.Black))
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text(BLOCKTEXT_ELSE)
        }
    }
}