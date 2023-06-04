package com.example.cleverex.presentation.screens.home

import android.os.Build
import android.util.Log
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
import com.example.cleverex.model.Bill
import com.example.cleverex.presentation.components.BillHolder
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
    onClick: (String) -> Unit
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
//                            localDate =  localDate,
                            weekOfYear = weekOfYear,
                            weekBudget = weekBudget,
                            bills = bills
                        )
                    }
                }
                items(
                    items = bills,
                    key = { it._id.toString() }
                ) {

                    BillHolder(
                        bill = it,
                        onClick = onClick
                    )
                }
            }
        }
    } else {
        EmptyPage()
    }
}

@Composable
fun WeekStats(

) {

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

    //ChatGPT FTW
//    val calendar = Calendar.getInstance()
//    val toInstant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
////    val toInstant = Instant.from(localDateTime.atZone(ZoneId.systemDefault()))
//    calendar.time = Date.from(toInstant)
//    calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
//    calendar.set(
//        Calendar.DAY_OF_WEEK,
//        calendar.firstDayOfWeek
//    ) // Set the calendar to the start of the current week
//    val firstDayOfWeekOfDayOfMonth =
//        calendar.get(Calendar.DAY_OF_MONTH) // Get the day of the month of the start of the week
//    Log.d(
//        "",
//        "date: ${localDate.dayOfMonth} Day of month of start of current week: $firstDayOfWeekOfDayOfMonth"
//    )
//    Log.d(
//        "",
//        "date: ${localDate.dayOfMonth} Day of month of end of current week: ${firstDayOfWeekOfDayOfMonth + 6}"
//    )

    val calendar = Calendar.getInstance()
    calendar.setWeekDate(2023, weekOfYear, Calendar.MONDAY)
    calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
    calendar.set(
        Calendar.DAY_OF_WEEK,
        calendar.firstDayOfWeek
    ) // Set the calendar to the start of the current week
    val localDate =  calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    Log.d("Current calendar date", "$localDate")


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
                String.format("%02d", localDate.dayOfMonth  )
                ,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Light
                )

            )
            Text(
                text =
                localDate.month.toString().take(3).lowercase()
                    .replaceFirstChar { it.titlecase() }
                ,
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
                String.format("%02d", localDate.dayOfMonth + 6 )
                ,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text =
                localDate.month.toString().take(3).lowercase()
                    .replaceFirstChar { it.titlecase() }
                ,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }

        // TODO rozdzielic to na dwa komponenty: Date, Statistics
        Row(
//            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "22,93 $",
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
                    text = "78,27 $",
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

@Preview(showBackground = true)
@Composable
fun EmptyPagePreview() {
    EmptyPage()
}

//@Preview(showBackground = true)
//@Composable
//fun WeekIndicatorPreview() {
//    WeekIndicator(localDate = LocalDate.now(), bills = listOf(), weekBudget = 0.0)
//}