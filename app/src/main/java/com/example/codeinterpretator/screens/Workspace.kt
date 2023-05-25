package com.example.codeinterpretator.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.codeinterpretator.R
import com.example.codeinterpretator.RenderBlock
import com.example.codeinterpretator.blocks.DeclarationOrAssignmentBlock
import com.example.codeinterpretator.blocks.DeclarationOrAssignmentBlockView
import com.example.codeinterpretator.blocks.OutputBlock
import com.example.codeinterpretator.blocks.OutputBlockView
import com.example.codeinterpretator.blocks.blockList
import com.example.codeinterpretator.createBlock
import com.example.codeinterpretator.executeCode
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
        //val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        var declaration = DeclarationOrAssignmentBlock()
        var output = OutputBlock()
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                Box(
                    modifier = Modifier.clickable {
                        createBlock(declaration)
                        scope.launch{
                            scaffoldState.drawerState.close()
                        }
                    }
                ) {
                    DeclarationOrAssignmentBlockView(declaration)
                }

                Box(
                    modifier = Modifier.clickable {
                        createBlock(output)
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
                LazyColumn() {
                    itemsIndexed(blockList) { index, item ->
                        RenderBlock(item)
                    }
                }
                Button(onClick = {
                    scope.launch { scaffoldState.drawerState.open() }
                }) {
                    Text("+", fontSize = 28.sp)
                }
            }
        }
    }
}