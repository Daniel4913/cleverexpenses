package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.ui.theme.Elevation
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import timber.log.Timber
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewContent(
    selectedBill: Bill?,
    billItems: List<BillItem>,
    paddingValues: PaddingValues,
    showPieChart: Boolean
) {
    val categorySpendingMap = createCategorySpendingMap(billItems)

    Row(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding() + 8.dp)
    ) {
        if (showPieChart) {
            PieChartParent(categorySpendingMap = categorySpendingMap)
        } else {
            CategorySpendingBarChart(categorySpendingMap = categorySpendingMap)
        }
    }


    if (billItems.isNotEmpty()) {
        Timber.d("billItems: ${billItems.size}")
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            items(
                items = billItems
            ) {
                ItemOverview(billItem = it)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun PieChartParent(
    categorySpendingMap: Map<String, Double>
) {

    val slices = categorySpendingMap.map { entry ->
        val (name, icon, hexColor) = entry.key.split(" ")

        PieChartData.Slice(entry.value.toFloat(), Color(hexColor.toULong()))
    }
    PieChart(
        pieChartData = PieChartData(slices = slices),
        modifier = Modifier.fillMaxSize(),
        animation = simpleChartAnimation(),
        sliceDrawer = SimpleSliceDrawer()
    )
}

@Composable
fun CategorySpendingBarChart(categorySpendingMap: Map<String, Double>) {
    val categories = categorySpendingMap.keys.toList()
    val spendingValues = categorySpendingMap.values.map { it.toFloat() }

    BarChart(
        barChartData = BarChartData(
            bars = categories.mapIndexed { index, label ->
                val categoryInfo = label.split(" ") // Split the label to get name, icon, and color
                val categoryName = "${categoryInfo[0]} ${categoryInfo[1]}"
                Timber.d("${Color(categoryInfo[2].toULong())}")
                val categoryColor = Color(categoryInfo[2].toULong())

                BarChartData.Bar(
                    label = categoryName,
                    value = spendingValues[index],
                    color = categoryColor
                )
            }
        ),
        labelDrawer = CategoryLabelDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(
            labelTextColor = MaterialTheme.colorScheme.onSurface,
            labelValueFormatter = { value -> "%.2f".format(value) },
            labelTextSize = 14.sp
        ),
        barDrawer = CategoryBarDrawer(),
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxSize()
    )
}

fun createCategorySpendingMap(billItems: List<BillItem>): Map<String, Double> {
    val categorySpendingMap = mutableMapOf<String, Double>()

    for (billItem in billItems) {
        for (category in billItem.categories) {
            val categoryValues = "${category.name} ${category.icon} ${category.categoryColor}"
            val itemsPrice = billItem.totalPrice

            val currentSpending = categorySpendingMap.getOrDefault(categoryValues, 0.0)
            categorySpendingMap[categoryValues] = currentSpending + itemsPrice
        }
    }

    return categorySpendingMap
}

@Composable
fun ItemOverview(billItem: BillItem) {
    Row {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = Shapes().medium)
//                .weight(2f)
            ,
            tonalElevation = Elevation.Level1
        ) {
            Row(
                modifier = Modifier.padding(start = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
//                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "${billItem.name} - ${billItem.quantity} - ${billItem.totalPrice}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
//                    Text(text = billItem.name)
//                    Text(text = billItem.quantity.toString())
//                    Text(text = billItem.totalPrice.toString())
                }
                Surface(
                    modifier = Modifier
//                        .fillMaxWidth()
                        .clip(shape = Shapes().large),
//                        .weight(1f),
                    tonalElevation = Elevation.Level3
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Spacer(modifier = Modifier.width(4.dp))
                        if (!billItem.categories.isNullOrEmpty()) {
                            Text(
                                text = billItem.categories[0].icon,
                                style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "category"
                        )
//                        Icon(
//                            imageVector = Icons.Rounded.Build,
//                            contentDescription = "category"
//                        )
//                        Icon(
//                            imageVector = Icons.Rounded.MailOutline,
//                            contentDescription = "category"
//                        )
//                        Icon(
//                            imageVector = Icons.Rounded.Edit,
//                            contentDescription = "category"
//                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ItemOverviewPreview() {
    ItemOverview(billItem = BillItem().apply {
        name = "Bushmills Whisk."
        price = 79.99
        quantity = 1.0
        unit = "szt"
        totalPrice = 79.99
//                        category = CategoryItem(
//                            name = Name(value = "Home"),
//                            icon = Icon(value = "√"),
//                            categoryColor = CategoryColor(
//                                value = 0xFF6E5E00)
//                        )
    })
}


data class CategoriesFromProducts(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val quantity: Int,
    val unit: String,
    val price: Double,
    val currency: String = "zł"
)




