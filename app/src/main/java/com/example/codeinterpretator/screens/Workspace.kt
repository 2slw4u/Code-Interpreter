package com.example.codeinterpretator.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.codeinterpretator.R
import com.example.codeinterpretator.RenderBlock
import com.example.codeinterpretator.blockList
import com.example.codeinterpretator.createBlock
import com.example.codeinterpretator.executeCode

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
            Row() {
                Button(onClick = {
                    createBlock()
                    Toast.makeText(context, blockList.size.toString(), Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Create Block")
                }

                Button(onClick = {
                    executeCode()
                    Toast.makeText(context, blockList.size.toString(), Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Run!")
                }

                Button(onClick = {
                    executeCode()
                    Toast.makeText(context, blockList.size.toString(), Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Console")
                }
            }

        }
    }
}