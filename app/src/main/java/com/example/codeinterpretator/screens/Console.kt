package com.example.codeinterpretator.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.codeinterpretator.RenderBlock
import com.example.codeinterpretator.blocks.Block
import com.example.codeinterpretator.blocks.InputBlock
import com.example.codeinterpretator.blocks.blockList
import com.example.codeinterpretator.createBlock
import com.example.codeinterpretator.executeCode
import com.example.codeinterpretator.ui.theme.Black
import com.example.codeinterpretator.ui.theme.INPUT_SEND
import com.example.codeinterpretator.ui.theme.White

object Console : Tab {
    val consoleLines = mutableStateListOf<String>()
    val inputBlocks = mutableStateListOf<InputBlock>()

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.CheckCircle)

            return remember {
                TabOptions(
                    index = 2u,
                    title = "Console",
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Box(modifier = Modifier
            .background(color = Black)
            .fillMaxSize()) {
            LazyColumn() {
                itemsIndexed(consoleLines) { index, item ->
                    Text(item, color = White)
                }
                items(inputBlocks) { item ->
                    TextSender(item)
                }
            }
        }
    }

    @Composable
    fun TextSender(block: InputBlock) {
        var value by remember { mutableStateOf("") }

        Column() {
            BasicTextField(
                value = value,
                textStyle = TextStyle(color = White),
                onValueChange = {
                    value = it
                    block.value = value
                }
            )
            Button(
                onClick = {
                    inputBlocks.remove(block)
                    block.continueExecution(block.temporaryVariables)
                }
            ) {
                Text(INPUT_SEND)
            }
        }
    }

    fun print(text: String) {
        consoleLines.add(text)
    }

    fun clear() {
        consoleLines.clear()
        inputBlocks.clear()
    }

    fun read(block: InputBlock) {
        inputBlocks.add(block)
    }
}