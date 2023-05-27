package com.example.codeinterpretator.blocks

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codeinterpretator.RenderBlock
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.DragTarget
import com.example.codeinterpretator.ui.theme.TAB

val blockList = mutableStateListOf<Block>()
open class Block {
    open public fun translateToRPN(): ArrayList<String> {
        return arrayListOf<String>()
    }
    open public fun execute(variables: HashMap<String, Any>) {}

    open public var partCount: Int = 1
    open public var nextBlock: Block? = null
    open public var parentBlock: NestingBlock? = null
    open public fun nestedPadding(): Int {
        return BETWEEN_BLOCK_DISTANCE + this.nestingLevel() * TAB
    }

    open public fun nestingLevel(): Int {
        if (parentBlock == null) {
            return 0
        }
        return parentBlock?.nestingLevel()?.plus(1) ?: 0
    }
}

open class NestingBlock : Block() {
    open public fun executeAfterNesting(variables: HashMap<String, Any>) {
        nextBlock?.execute(variables)
        if(nextBlock == null)
            parentBlock?.executeAfterNesting(variables)
    }
}

/*@Composable
fun NestedBlockColumn(blockList: ArrayList<Block>) {
    LazyColumn(
        modifier = Modifier.padding(start = 20.dp)
    ) {
        item { ReceiverBlockView(-1) }
        itemsIndexed(blockList) { index, item ->
            DragTarget(modifier = Modifier, dataToDrop = item, blockId = index) {
                RenderBlock(item)
            }
            ReceiverBlockView(index)
        }
    }
}*/