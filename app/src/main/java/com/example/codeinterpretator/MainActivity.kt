package com.example.codeinterpretator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codeinterpretator.ui.theme.CodeInterpretatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Workspace()
        }
        println("Hello world")
    }
}

@Preview
@Composable
fun Workspace() {
    val lazyListState: LazyListState = rememberLazyListState()
    Column() {
        AssignmentBlock()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentBlock() {
    var variableName by remember { mutableStateOf("") }
    var variableValue by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var variableType by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .border(BorderStroke(2.dp, Color.Black))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .border(BorderStroke(2.dp, Color.Black))) {
            TextButton(
                onClick = { dropdownExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (variableType.isNotEmpty()) {
                    Text(variableType)
                } else {
                    Text("Integer")
                }
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(onClick = {
                    variableType = "Integer"
                    dropdownExpanded = false
                },
                    text = { Text("Integer") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "String"
                    dropdownExpanded = false
                },
                text = { Text("String") }
                )
                DropdownMenuItem(onClick = {
                    variableType = "Boolean"
                    dropdownExpanded = false
                },
                    text = { Text("Boolean") }
                )
            }
        }

        TextField(
            value = variableName,
            onValueChange = { variableName = it },
            modifier = Modifier.weight(1f).height(60.dp),
            label = { Text("Name") }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(60.dp).padding(10.dp)
            ) {
            Text("=")
        }

        TextField(
            value = variableValue,
            onValueChange = { variableValue = it },
            modifier = Modifier.weight(1f).height(60.dp),
            label = { Text("Value") }
        )
    }
}