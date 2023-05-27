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
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.codeinterpretator.blocks.AssignmentBlock
import com.example.codeinterpretator.blocks.AssignmentBlockView
import com.example.codeinterpretator.blocks.Block
import com.example.codeinterpretator.blocks.DeclarationOrAssignmentBlock
import com.example.codeinterpretator.blocks.DeclarationOrAssignmentBlockView
import com.example.codeinterpretator.blocks.InputBlock
import com.example.codeinterpretator.blocks.InputBlockView
import com.example.codeinterpretator.blocks.OutputBlock
import com.example.codeinterpretator.blocks.OutputBlockView
import com.example.codeinterpretator.blocks.blockList
import com.example.codeinterpretator.screens.Console
import com.example.codeinterpretator.screens.Workspace
import com.example.codeinterpretator.ui.theme.CodeInterpretatorTheme
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabNavigator(Workspace) {
                Scaffold(
                    content = { PaddingValues ->
                            CurrentTab()
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            content = {
                                Icon(Icons.Filled.PlayArrow, contentDescription = "Run") },
                            onClick = {
                                Console.clear()
                                executeCode()
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigation {
                            TabNavigationItem(Workspace)
                            TabNavigationItem(Console)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
    )
}

@Composable
fun RenderBlock(block: Block) {
    when(block) {
        is DeclarationOrAssignmentBlock -> {
            DeclarationOrAssignmentBlockView(block)
        }
        is AssignmentBlock -> {
            AssignmentBlockView(block)
        }
        is InputBlock -> {
            InputBlockView(block)
        }
        is OutputBlock -> {
            OutputBlockView(block)
        }
    }
}

fun createBlock(block: Block, at: Int) {
    blockList.add(at, block)
}

fun deleteBlock(at: Int) {
    blockList.removeAt(at)
}

fun executeCode() {
    var variables = HashMap<String, Any>()
    for (currentBlock: Block in blockList) {
        currentBlock.execute(variables)
    }
}
