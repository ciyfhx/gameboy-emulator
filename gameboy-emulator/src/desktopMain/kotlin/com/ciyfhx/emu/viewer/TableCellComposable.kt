package com.ciyfhx.emu.viewer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RowScope.TableHeaderCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .border(.5.dp, Color.Black)
            .weight(weight)
            .padding(5.dp)
    )
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        fontSize = 15.sp,
        modifier = Modifier
            .border(.5.dp, Color.Black)
            .weight(weight)
            .padding(5.dp)
    )
}