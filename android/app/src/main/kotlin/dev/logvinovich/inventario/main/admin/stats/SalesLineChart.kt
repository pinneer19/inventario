package dev.logvinovich.inventario.main.admin.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.domain.model.SalesByDate
import dev.logvinovich.inventario.ui.util.dateFormatter
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import kotlinx.datetime.format

@Composable
fun SalesLineChart(data: List<SalesByDate>) {
    if (data.isEmpty()) {
        Text("No sales data", modifier = Modifier.padding(16.dp))
        return
    }

    // Extract amounts and formatted date labels
    val values = data.map { it.totalAmount.toDouble() }
    val labels = data.map { it.date.date.format(dateFormatter) }

    val line = Line(
        label = "Sales by Date",
        values = values,
        color = SolidColor(Color(0xFF3F51B5)) // Optional styling
    )

    LineChart(
        data = listOf(line),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp),
        labelProperties = LabelProperties(
            enabled = true,
            labels = labels,
        ),
        gridProperties = GridProperties(
            xAxisProperties = GridProperties.AxisProperties()
        ),
        dotsProperties = DotProperties(enabled = true),
        indicatorProperties = HorizontalIndicatorProperties(enabled = true),
        curvedEdges = true,
        maxValue = (values.maxOrNull() ?: 0.0) * 1.2, // add headroom
        minValue = 0.0
    )
}