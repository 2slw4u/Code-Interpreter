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
import androidx.compose.ui.unit.dp
import com.example.codeinterpretator.interpreter.ExpressionToRPNConverter
import com.example.codeinterpretator.interpreter.convertToIntOrNull
import com.example.codeinterpretator.interpreter.extractIndexSubstring
import com.example.codeinterpretator.interpreter.getType
import com.example.codeinterpretator.interpreter.interpretRPN
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.ui.theme.BETWEEN_BLOCK_DISTANCE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_NAME
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_NAMES
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VALUE
import com.example.codeinterpretator.ui.theme.BLOCKLABEL_VALUES
import com.example.codeinterpretator.ui.theme.BLOCK_HEIGHT
import com.example.codeinterpretator.ui.theme.BORDER
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.EQUALSIGN
import com.example.codeinterpretator.ui.theme.ERROR_ARRAY_SIZE_IS_NEGATIVE
import com.example.codeinterpretator.ui.theme.ERROR_ARRAY_SIZE_NOT_INTEGER
import com.example.codeinterpretator.ui.theme.ERROR_ARRAY_TYPE_NOT_DECLARED
import com.example.codeinterpretator.ui.theme.ERROR_ARRAY_UNCOMPATIBLE_TYPES
import com.example.codeinterpretator.ui.theme.ERROR_CONTAINING_RESTRICTED_SYMBOLS
import com.example.codeinterpretator.ui.theme.ERROR_NO_VARIABLE_NAME
import com.example.codeinterpretator.ui.theme.ERROR_REDECLARING_VARIABLES
import com.example.codeinterpretator.ui.theme.ERROR_TOO_MANY_VARIABLES_FOR_ARRAY
import com.example.codeinterpretator.ui.theme.ERROR_USING_KEYWORDS_IN_VARIABLE_NAME
import com.example.codeinterpretator.ui.theme.SINGLE_WEIGHT
import com.example.codeinterpretator.ui.theme.TYPENAME_BOOL
import com.example.codeinterpretator.ui.theme.TYPENAME_CHAR
import com.example.codeinterpretator.ui.theme.TYPENAME_DOUBLE
import com.example.codeinterpretator.ui.theme.TYPENAME_INT
import com.example.codeinterpretator.ui.theme.TYPENAME_STRING
import com.example.codeinterpretator.ui.theme.TYPE_LABEL_WIDTH
import com.example.codeinterpretator.ui.theme.White

class ArrayDeclarationAndAssignmentBlock : Block() {
    val unusableNames: Array<String> =
        arrayOf("null", "if", "for", "fun", "Int", "String", "Bool", "Double", "Float", "Char")
    var variableNames: String =
        "" // Здесь мы берём названия массивов из соответствующего поля блока

    // декларации
    var variableType: String =
        TYPENAME_INT // Здесь мы берём тип переменной из соответствующего поля блока декларации
    var variableValues: String = ""
    val typesExamples =
        hashMapOf<String, Any>(
            "Int" to 0,
            "String" to "a",
            "Bool" to true,
            "Double" to 0.5,
            "Char" to 'c'
        )

    private fun createTypedArray(size: Any): Array<Any> {
        return when (variableType) {
            "String" -> {
                Array(size.toString().toDouble().toInt()) { "" }
            }

            "Int" -> {
                Array(size.toString().toDouble().toInt()) { 0 }
            }

            "Boolean" -> {
                Array(size.toString().toDouble().toInt()) { false }
            }

            "Char" -> {
                Array(size.toString().toDouble().toInt()) { '0' }
            }

            "Double" -> {
                Array(size.toString().toDouble().toInt()) { 0.0 }
            }

            else -> {
                Console.print(ERROR_ARRAY_TYPE_NOT_DECLARED)
                Array(size.toString().toDouble().toInt()) { 0 }
            }
        }
    }

    private fun reformArray(
        values: List<String>,
        currentArray: Array<Any>,
        variables: HashMap<String, Any>
    ): Array<Any> {
        val converter = ExpressionToRPNConverter()
        val result = currentArray
        for (valueIndex in values.indices) {
            if (valueIndex < result.size) {
                val currentValue =
                    interpretRPN(
                        variables,
                        converter.convertExpressionToRPN(values[valueIndex])
                    )
                if (typesExamples[variableType]!!::class == currentValue::class) {
                    result[valueIndex] = currentValue
                } else {
                    Console.print(
                        ERROR_ARRAY_UNCOMPATIBLE_TYPES + variableType + " ; " + getType(
                            result
                        )
                    )
                }
            } else {
                Console.print(ERROR_TOO_MANY_VARIABLES_FOR_ARRAY)
            }
        }
        return result
    }

    private fun getAllValues(): List<String> {
        val result = mutableListOf<String>()
        var ignoreSymbols = false
        var writeSymbols = false
        var value = ""
        for (i in variableValues.indices) {
            if (variableValues[i] == '\"') {
                ignoreSymbols = (ignoreSymbols == false)
            }
            if (ignoreSymbols == false && variableValues[i] == '(') {
                writeSymbols = true
            } else if (ignoreSymbols == false && variableValues[i] == ')') {
                writeSymbols = false
                result.add(value)
                value = ""
            } else if (writeSymbols == true) {
                value += variableValues[i]
            }
        }
        return result
    }

    override public fun execute(variables: HashMap<String, Any>) {
        val allNames = variableNames.replace(" ", "").split(",")
        for (nameIndex in allNames.indices) {
            var converter = ExpressionToRPNConverter()
            val size =
                interpretRPN(
                    variables,
                    converter.convertExpressionToRPN(
                        extractIndexSubstring(allNames[nameIndex], '(', ')')
                    )
                )
            val variableName = allNames[nameIndex].substringBefore('(')
            if (variableName == "") {
                Console.print(ERROR_NO_VARIABLE_NAME)
            } else if (variables.containsKey(variableName)) {
                Console.print(ERROR_REDECLARING_VARIABLES)
            } else if (unusableNames.contains(variableName)) {
                Console.print(ERROR_USING_KEYWORDS_IN_VARIABLE_NAME)
            } else if (!variableName.matches(Regex("[a-zA-Z_][a-zA-Z0-9_]*"))) {
                Console.print(ERROR_CONTAINING_RESTRICTED_SYMBOLS)
            } else if (convertToIntOrNull(size.toString()) == null) {
                Console.print(ERROR_ARRAY_SIZE_NOT_INTEGER)
            } else if (convertToIntOrNull(size.toString())!! < 0) {
                Console.print(ERROR_ARRAY_SIZE_IS_NEGATIVE)
            } else {
                var createdArray: Array<Any> = createTypedArray(size)
                createdArray = reformArray(getAllValues(), createdArray, variables)
                variables.put(variableName, createdArray)
            }
        }

        nextBlock?.execute(variables)
        if (nextBlock == null)
            parentBlock?.executeAfterNesting(variables)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrayDeclarationAndAssignmentBlockView(block: ArrayDeclarationAndAssignmentBlock) {
    var variableNames by remember { mutableStateOf("") }
    var variableValues by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    variableNames = block.variableNames
    variableValues = block.variableValues

    Row(
        modifier = Modifier
            .padding(start = block.nestedPadding().dp, end = BETWEEN_BLOCK_DISTANCE.dp)
            .border(BorderStroke(BORDER.dp, Black))
            .background(color = White)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(TYPE_LABEL_WIDTH.dp)
                .height(BLOCK_HEIGHT.dp)
        ) {
            TextButton(
                onClick = { dropdownExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(block.variableType)
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_INT
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_INT) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_STRING
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_STRING) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_BOOL
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_BOOL) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_DOUBLE
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_DOUBLE) }
                )
                DropdownMenuItem(onClick = {
                    block.variableType = TYPENAME_CHAR
                    dropdownExpanded = false
                },
                    text = { Text(TYPENAME_CHAR) }
                )
            }
        }

        TextField(
            value = variableNames,
            onValueChange = {
                variableNames = it
                block.variableNames = variableNames
            },
            modifier = Modifier
                .weight(SINGLE_WEIGHT)
                .height(BLOCK_HEIGHT.dp),
            label = { Text(BLOCKLABEL_NAMES) }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(BLOCK_HEIGHT.dp)
                .padding(BETWEEN_BLOCK_DISTANCE.dp)
        ) {
            Text(EQUALSIGN)
        }

        TextField(
            value = variableValues,
            onValueChange = {
                variableValues = it
                block.variableValues = variableValues
            },
            modifier = Modifier
                .weight(SINGLE_WEIGHT)
                .height(BLOCK_HEIGHT.dp),
            label = { Text(BLOCKLABEL_VALUES) }
        )
    }
}