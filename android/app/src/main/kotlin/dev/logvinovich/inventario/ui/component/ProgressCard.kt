package dev.logvinovich.inventario.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.R

@Composable
fun ProgressCard(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)

                    Text(
                        text = stringResource(id = R.string.loading),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}