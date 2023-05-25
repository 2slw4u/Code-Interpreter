package com.example.codeinterpretator.blocks

open class Block {
    open public fun translateToRPN(): ArrayList<String> {
        return arrayListOf<String>()
    }
    open public fun execute(variables: HashMap<String, Any>) {}
}