package com.example.codeinterpretator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codeinterpretator.blocks.DeclarationBlock
import com.example.codeinterpretator.blocks.DeclarationBlockView
import com.example.codeinterpretator.ui.theme.CodeInterpretatorTheme

val blockList = mutableStateListOf<Any>(DeclarationBlock(), DeclarationBlock())
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Workspace()
        }
        println("Hello world")
    }
}

@Composable
fun RenderBlock(block: Any) {
    when(block) {
        is DeclarationBlock -> {
            DeclarationBlockView(block)
        }
    }
}

fun createBlock() {
    val newBlock = DeclarationBlock()
    blockList.add(newBlock)
}


@Preview
@Composable
fun Workspace() {
    LazyColumn() {
        itemsIndexed(blockList) {
                index, item ->
            RenderBlock(item)
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        Button(onClick = {
            createBlock()
            Toast.makeText(context, blockList.size.toString(), Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Нажми меня")
        }
    }
}
