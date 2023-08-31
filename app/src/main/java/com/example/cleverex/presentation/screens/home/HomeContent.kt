package com.example.cleverex.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cleverex.domain.Bill
import com.example.cleverex.presentation.components.BillPreviewHolder
import com.example.cleverex.ui.theme.Elevation
import java.time.*
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    weekBudget: Double,
    datedBills: Map<Int, List<Bill>>,
    onBillClicked: (String) -> Unit,
    onWeekIndicatorClicked: (String) -> Unit
) {
    if (datedBills.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .navigationBarsPadding()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // TODO sort bills by week MON-SUN
            datedBills.forEach { (weekOfYear, bills) ->
                val calendar = Calendar.getInstance()
                calendar.firstDayOfWeek = Calendar.MONDAY
                calendar.set(
                    Calendar.DAY_OF_WEEK,
                    calendar.firstDayOfWeek
                )
                stickyHeader(key = weekOfYear) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        WeekIndicator(
                            weekOfYear = weekOfYear,
                            weekBudget = weekBudget,
                            bills = bills,
                        )
                    }
                }
                items(
                    items = bills,
                    key = { it._id.toString() }
                ) {
                    BillPreviewHolder(
                        bill = it,
                        onClick = onBillClicked
                    )
                }
            }
        }
    } else {
        EmptyPage()
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun WeekIndicator(
//    localDate: LocalDate,
    weekOfYear: Int,
    bills: List<Bill>,
    weekBudget: Double
) {
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }

    val calendar = Calendar.getInstance()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    calendar.setWeekDate(currentYear, weekOfYear, Calendar.MONDAY)
    calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
    calendar.set(
        Calendar.DAY_OF_WEEK,
        calendar.firstDayOfWeek
    )
    val startOfWeek = calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    calendar.add(Calendar.DAY_OF_WEEK, 6)
    val endOfWeek = calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.onGloballyPositioned {
                componentHeight = with(localDensity) { it.size.height.toDp() }
            }) {
            Text(
                text =
                String.format("%02d", startOfWeek.dayOfMonth),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Light
                )

            )
            Text(
                text =
                startOfWeek.month.toString().take(3).lowercase()
                    .replaceFirstChar { it.titlecase() },
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }
        // Line between dates
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text =
                String.format("%02d", endOfWeek.dayOfMonth + 6),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text =
                endOfWeek.month.toString().take(3).lowercase()
                    .replaceFirstChar { it.titlecase() },
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }

        // TODO rozdzielic to na dwa komponenty: Date, Statistics
        val totalSpent = bills.sumOf { it.price ?: 0.0 }
        val remainingBudget = weekBudget - totalSpent
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = String.format("%.2f $", totalSpent),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Light
                    )
                )
                Text(
                    text = "Spent",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.Light
                    )
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = String.format("%.2f $", remainingBudget),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Light
                    )
                )
                Text(
                    text = "Remains",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}

@Composable
fun EmptyPage(
    title: String = "No Expenses",
    subtitle: String = "Add your first Bill!"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Normal
            )
        )
    }
}
