package com.example.codeinterpretator.blocks

import androidx.compose.runtime.mutableStateListOf

val blockList = mutableStateListOf<Block>()
open class Block {
    open public fun translateToRPN(): ArrayList<String> {
        return arrayListOf<String>()
    }
    open public fun execute(variables: HashMap<String, Any>) {}

    open public var partCount: Int = 1

    open public var nextBlock: Block? = null
}