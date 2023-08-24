package com.example.cleverex.presentation.screens.addBill


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cleverex.R
import com.example.cleverex.domain.Bill
import com.example.cleverex.presentation.components.DisplayAlertDialog
import com.example.cleverex.util.toInstant
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillTopBar(
    selectedBill: Bill?,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit
) {

    val dateDialog = rememberSheetState()
    val timeDialog = rememberSheetState()

    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    val formattedCurrentDate = remember(key1 = currentDate) {
        DateTimeFormatter
//            .ofPattern("dd MMMM EEEE") // 23 AUGUST WEDNESDAY
            .ofPattern("dd-MM EEEE") // 23 08 WEDNESDAY
            .format(currentDate)
            .uppercase()
    }
    val formattedCurrentTime = remember(key1 = currentTime) {
        DateTimeFormatter
            .ofPattern("HH:mm") // 19:29
            .format(currentTime)
            .uppercase()
    }

    var dateTimeUpdated by remember {
        mutableStateOf(false)
    }

    val selectedBillDateTime = remember(selectedBill) {
        if (selectedBill != null) {
            SimpleDateFormat("dd MMMM, EEEE HH:mm", Locale.getDefault())
                .format(
                    Date.from(selectedBill.billDate.toInstant())
                )
                //todo oczywiscie replace first char nie dziala
//                .uppercase()
                .replaceFirstChar { it.titlecase() }
        } else {
            "Shopping date"
        }
    }

    MediumTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Navigate back icon"
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = selectedBill?.shop ?: "Add bill",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    )
                )

                Spacer(modifier = Modifier.width(16.dp))
                // DateTime
                Text(
                    text = if (selectedBill != null && dateTimeUpdated) {
                        "$formattedCurrentDate, $formattedCurrentTime"
                    } else if (selectedBill != null) {
                        selectedBillDateTime
                    } else {
                        "$formattedCurrentDate, $formattedCurrentTime"
                    },
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    textAlign = TextAlign.End,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column() {
                    //Shop
                    Text(
                        text = "Shop",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        ),
                    )
                    //Address
                    Text(
                        text = "address",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        ),
                    )
                }

            }
        }, actions = {
            IconButton(onClick = {
                changeIconTint()
            }) {
                Image(
                    painterResource(id = R.drawable.ai_color),
                    contentDescription = "Ai icon",
                    Modifier.padding(9.dp),
                )
            }
            if (dateTimeUpdated) {
                IconButton(onClick = {
                    currentDate = LocalDate.now()
                    currentTime = LocalTime.now()
                    dateTimeUpdated = false
                    onDateTimeUpdated(
                        ZonedDateTime.of(
                            currentDate,
                            currentTime,
                            ZoneId.systemDefault()
                        )
                    )
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                IconButton(onClick = {
                    dateDialog.show()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = "Date icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "Location icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            if (selectedBill != null) {
                DeleteBillAction(
                    selectedBill = selectedBill,
                    onDeleteConfirmed = onDeleteConfirmed
                )
            }

        }
    )

    CalendarDialog(
        state = dateDialog, selection = CalendarSelection.Date { localDate ->
            currentDate = localDate
            timeDialog.show()

        }, config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        )
    )



    ClockDialog(state = timeDialog, selection = ClockSelection.HoursMinutes { hours, minutes ->
        currentTime = LocalTime.of(hours, minutes)
        dateTimeUpdated = true
        onDateTimeUpdated(
            ZonedDateTime.of(
                currentDate,
                currentTime,
                ZoneId.systemDefault()
            )
        )
    })
}

fun changeIconTint() {
}


@Composable
fun DeleteBillAction(
    selectedBill: Bill?,
    onDeleteConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Delete")
            }, onClick = {
                openDialog = true
                expanded = false
            }
        )
    }
    DisplayAlertDialog(
        title = "Delete",
        message = "Are you sure you want to permanently delete this diary note '${selectedBill?.price}'?",
        dialogOpened = openDialog,
        onDialogClosed = { openDialog = false },
        onYesClicked = onDeleteConfirmed
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}