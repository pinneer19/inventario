package dev.logvinovich.inventario.main.manager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.R

@Composable
fun EmptyWarehouseListContent(
    visible: Boolean,
    onRefresh: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.manager_no_warehouses),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRefresh,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 30.dp)
            ) {
                Text(text = stringResource(R.string.refresh))
            }
        }
    }
}