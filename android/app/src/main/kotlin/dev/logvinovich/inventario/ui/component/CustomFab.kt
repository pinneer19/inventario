package dev.logvinovich.inventario.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomFab(
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier.size(65.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = Icons.Default.Add,
            contentDescription = contentDescription
        )
    }
}