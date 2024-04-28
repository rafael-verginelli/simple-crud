package com.rafver.core_ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rafver.core_ui.theme.SimpleCRUDTheme

@Composable
fun LoadingSpinnerWidget(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(modifier = modifier)
    }
}

@Preview
@Composable
private fun PreviewLoadingSpinnerWidget() {
    SimpleCRUDTheme {
        LoadingSpinnerWidget()
    }
}