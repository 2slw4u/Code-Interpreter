package com.example.codeinterpretator.blocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.codeinterpretator.R
import com.example.codeinterpretator.createBlock
import com.example.codeinterpretator.deleteBlock
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.screens.Workspace
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCK_HEIGHT
import com.example.codeinterpretator.ui.theme.DragTarget
import com.example.codeinterpretator.ui.theme.DropTarget
import com.example.codeinterpretator.ui.theme.Transparent

@Composable
fun ReceiverBlockView(receiverBlockId: Int) {
    var alreadyPlaced = false
    DropTarget<Block>(
        modifier = Modifier
            .fillMaxWidth()
    ) { isInBound, block, oldBlockId ->
        val expandedHeight = BETWEEN_BLOCK_DISTANCE * 2 + BLOCK_HEIGHT * (block?.partCount ?: 1)
        var height = if (isInBound) expandedHeight else BETWEEN_BLOCK_DISTANCE
        block?.let {
            if (isInBound && !alreadyPlaced) {
                if (oldBlockId < receiverBlockId) {
                    createBlock(
                        block,
                        receiverBlockId + 1,
                        Workspace.parentBlock,
                        Workspace.additionalList
                    )
                    deleteBlock(oldBlockId)
                    Console.print("from $oldBlockId")
                    Console.print("to $receiverBlockId")
                    Console.print("if")
                } else {
                    createBlock(
                        block,
                        receiverBlockId + 1,
                        Workspace.parentBlock,
                        Workspace.additionalList
                    )
                    deleteBlock(oldBlockId + 1)
                    Console.print("from $oldBlockId")
                    Console.print("to $receiverBlockId")
                    Console.print("else")
                }
                height = BETWEEN_BLOCK_DISTANCE
                alreadyPlaced = true
            } else {
                createBlock(block, oldBlockId, Workspace.parentBlock, Workspace.additionalList)
            }
        }

        Box(
            modifier = Modifier
                .height(height.dp)
                .background(color = Transparent)
                .fillMaxWidth()
        )
    }
}