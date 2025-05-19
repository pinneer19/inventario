package dev.logvinovich.inventario.main.sale

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.ui.component.TextFieldComponent
import dev.logvinovich.inventario.ui.util.toFormattedString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterRow(
    warehouses: List<Warehouse>,
    selectedWarehouseId: Long?,
    onWarehouseChange: (Long?) -> Unit,
    dateRange: Pair<Long?, Long?>,
    onDateRangeChange: (Pair<Long?, Long?>) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = dateRange.first,
        initialSelectedEndDateMillis = dateRange.second,
        initialDisplayMode = DisplayMode.Input
    )
    val focusRequester = remember { FocusRequester() }
    var expandedMenu by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val selectedWarehouse = warehouses.find { it.id == selectedWarehouseId }

        Box(modifier = Modifier.weight(0.4f)) {
            TextFieldComponent(
                value = selectedWarehouse?.name ?: "All",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Warehouse"
                    )
                },
                readOnly = true,
                placeholder = "Hi",
                label = "Warehouse",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .pointerInput(selectedWarehouse) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent =
                                waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) {
                                expandedMenu = true
                            }
                        }
                    },
            )

            DropdownMenu(
                expanded = expandedMenu,
                offset = DpOffset(5.dp, 5.dp),
                onDismissRequest = { expandedMenu = false; focusManager.clearFocus() }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.all)) },
                    onClick = {
                        onWarehouseChange(null)
                        expandedMenu = false
                        focusManager.clearFocus()
                    }
                )
                warehouses.forEach { warehouse ->
                    HorizontalDivider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp))

                    DropdownMenuItem(
                        text = { Text(warehouse.name) },
                        onClick = {
                            onWarehouseChange(warehouse.id)
                            expandedMenu = false
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }

        TextFieldComponent(
            value = dateRange.toFormattedString(),
            onValueChange = {},
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Picker",
                )
            },
            readOnly = true,
            modifier = Modifier
                .weight(0.6f)
                .focusRequester(focusRequester)
                .pointerInput(dateRange) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent =
                            waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showDatePicker = true
                        }
                    }
                },
            label = stringResource(R.string.date_range),
            placeholder = stringResource(R.string.from_to)
        )
    }


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false; focusManager.clearFocus() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateRangeChange(
                            Pair(
                                dateRangePickerState.selectedStartDateMillis,
                                dateRangePickerState.selectedEndDateMillis
                            )
                        )
                        showDatePicker = false
                        focusManager.clearFocus()
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false; focusManager.clearFocus() }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = null
            )
        }
    }
}