package com.example.codeinterpretator.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.codeinterpretator.R
import com.example.codeinterpretator.RenderBlock
import com.example.codeinterpretator.blocks.AssignmentBlock
import com.example.codeinterpretator.blocks.AssignmentBlockView
import com.example.codeinterpretator.blocks.Block
import com.example.codeinterpretator.blocks.DeclarationOrAssignmentBlock
import com.example.codeinterpretator.blocks.DeclarationOrAssignmentBlockView
import com.example.codeinterpretator.blocks.InputBlock
import com.example.codeinterpretator.blocks.InputBlockView
import com.example.codeinterpretator.blocks.OutputBlock
import com.example.codeinterpretator.blocks.OutputBlockView
import com.example.codeinterpretator.blocks.ReceiverBlockView
import com.example.codeinterpretator.blocks.blockList
import com.example.codeinterpretator.createBlock
import com.example.codeinterpretator.executeCode
import com.example.codeinterpretator.ui.theme.DRAWER_TITLE
import com.example.codeinterpretator.ui.theme.DragTarget
import com.example.codeinterpretator.ui.theme.LongPressDraggable
import com.example.codeinterpretator.ui.theme.TITLE_ASSIGNMENT_BLOCK
import com.example.codeinterpretator.ui.theme.TITLE_DECLARATION_BLOCK
import com.example.codeinterpretator.ui.theme.TITLE_INPUT_BLOCK
import com.example.codeinterpretator.ui.theme.TITLE_OUTPUT_BLOCK
import kotlinx.coroutines.launch

object Workspace : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Edit)

            return remember {
                TabOptions(
                    index = 1u,
                    title = "Workspace",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        var declaration = DeclarationOrAssignmentBlock()
        var assignment = AssignmentBlock()
        var output = OutputBlock()
        var input = InputBlock()

        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                Text(
                    DRAWER_TITLE,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Text(TITLE_DECLARATION_BLOCK)
                Box(
                    modifier = Modifier
                        .clickable {
                        createBlock(declaration, blockList.size)
                        scope.launch{
                            scaffoldState.drawerState.close()
                        }
                    }
                ) {
                    DeclarationOrAssignmentBlockView(declaration)
                }

                Text(TITLE_ASSIGNMENT_BLOCK)
                Box(
                    modifier = Modifier.clickable {
                        createBlock(assignment, blockList.size)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                ) {
                    AssignmentBlockView(assignment)
                }

                Text(TITLE_INPUT_BLOCK)
                Box(
                    modifier = Modifier.clickable {
                        createBlock(input, blockList.size)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                ) {
                    InputBlockView(input)
                }

                Text(TITLE_OUTPUT_BLOCK)
                Box(
                    modifier = Modifier.clickable {
                        createBlock(output, blockList.size)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                ) {
                    OutputBlockView(output)
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                    Column() {
                        LazyColumn() {
                            item { ReceiverBlockView(-1) }
                            itemsIndexed(blockList) { index, item ->
                                DragTarget(modifier = Modifier, dataToDrop = item, blockId = index) {
                                    RenderBlock(item)
                                }
                                ReceiverBlockView(index)
                            }
                        }
                    }


                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Button(
                            onClick = { scope.launch { scaffoldState.drawerState.open() } },
                            modifier = Modifier.padding(bottom = 70.dp, start = 10.dp)
                        ) {
                            Text("+", fontSize = 28.sp)
                        }
                    }

                }
            }
        }
    }
}