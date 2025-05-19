package dev.logvinovich.inventario.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.domain.model.ChatMessage
import dev.logvinovich.inventario.ui.util.timeFormatter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

@Composable
fun MessageCard(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor) =
        if (message.isOwnMessage) {
            MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        } else MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = if (message.isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.senderUsername,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = LocalDateTime.parse(message.timestamp).time.format(timeFormatter),
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}