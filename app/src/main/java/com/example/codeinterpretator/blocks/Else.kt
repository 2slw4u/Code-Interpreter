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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeinterpretator.screens.Workspace
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKTEXT_ELSE
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ElseBlock : NestingBlock() {
    
}

@Composable
fun ElseBlockView(block: ElseBlock, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    Column() {
        ElsePart(block)

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
                Text("+", fontSize = 10.sp)
            }
        }

        /*LazyColumn(modifier = Modifier.padding(start = tab.dp)) {

        }*//*LazyColumn(
                modifier = Modifier.padding(start = tab.dp)
            ) {
                item {Text("hi")}
                item {Text("hi")}
            }*//*LazyColumn(
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

@Composable
fun ElsePart(block: ElseBlock) {
    Row(modifier = Modifier.run {
        padding(start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(
                BorderStroke(2.dp, Black)
            )
            .background(color = White)
            .fillMaxWidth()
    }) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .height(60.dp)
                .padding(10.dp)
        ) {
            Text(BLOCKTEXT_ELSE)
        }
    }
}