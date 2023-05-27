package com.example.codeinterpretator.blocks

import com.example.codeinterpretator.interpreter.ExpressionToRPNConverter
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.ui.theme.ERROR_NOT_BOOLEAN_TYPE

class WhileBlock : Block() {
    var ifCorrect: ArrayList<Block> = arrayListOf<Block>()
    var expression: String =
        "8 > 10" // Здесь мы берём выражение переменной из соответствующего поля блока

    override public fun translateToRPN(): ArrayList<String> {
        val converter = ExpressionToRPNConverter()
        return converter.convertExpressionToRPN(expression)
    }

    override public fun execute(variables: HashMap<String, Any>) {
        var result = interpretRPN(variables, this.translateToRPN())
        val variablesToRemove = arrayListOf<String>()
        var currentVariablesSize = variables.size
        while(true) {
            if (!(result is Boolean)) {
                Console.print(ERROR_NOT_BOOLEAN_TYPE)
                break
            }
            else if (result == false) {
                break
            }
            else {
                for (i: Block in ifCorrect) {
                    i.execute(variables)
                    if (variables.size > currentVariablesSize) {
                        variablesToRemove.add(variables.entries.last().key)
                        currentVariablesSize = variables.size
                    }
                }
                result = interpretRPN(variables, this.translateToRPN())
            }
        }
        for (key in variablesToRemove) {
            variables.remove(key)
        }
    }

    public fun addBlockToCorrect(new: Block) {
        ifCorrect.add(new)
    }
}