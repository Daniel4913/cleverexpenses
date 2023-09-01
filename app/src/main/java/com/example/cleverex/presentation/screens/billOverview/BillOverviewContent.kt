package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.browseCategory.CategoryRealm
import com.example.cleverex.ui.theme.Elevation
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import timber.log.Timber
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewContent(
    selectedBill: Bill?,
    billItems: List<BillItem>,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    paddingValues: PaddingValues
) {
    val categorySpendingMap = createCategorySpendingMap(billItems)



    Row(modifier = Modifier.height(300.dp).fillMaxWidth()) {
        CategorySpendingChart(categorySpendingMap = categorySpendingMap)
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

//@Composable
//fun CategorySpendingChart(categorySpendingMap: Map<CategoryRealm, Double>) {
//    val categories = categorySpendingMap.keys.toList()
//    val spendingValues = categorySpendingMap.values.map { it.toFloat() }
//
//    BarChart(
//        barChartData = BarChartData(
//            bars = categories.mapIndexed { index, categoryName ->
//                BarChartData.Bar(
//                    label = categoryName.icon,
//                    value = spendingValues[index],
//                    color = Color(categoryName.categoryColor)
//                )
//            }
//        ),
//        labelDrawer = CategoryLabelDrawer(),
//        yAxisDrawer = SimpleYAxisDrawer(
//            labelTextColor = Color.Black,
//            labelValueFormatter = { value -> "%.2f".format(value) },
//            labelTextSize = 14.sp
//        ),
//        barDrawer = CategoryBarDrawer(),
//        modifier = Modifier
//            .padding(bottom = 20.dp)
//            .fillMaxSize()
//    )
//}
//
//fun createCategorySpendingMap(billItems: List<BillItem>): Map<CategoryRealm, Double> {
//    val categorySpendingMap = mutableMapOf<CategoryRealm, Double>()
//
//    for (billItem in billItems) {
//        for (category in billItem.categories) {
//            val categoryName = category.name
//            val itemPrice = billItem.totalPrice
//
//            val currentSpending = categorySpendingMap.getOrDefault(category, 0.0)
//            categorySpendingMap[category] = currentSpending + itemPrice
//        }
//    }
//
//    return categorySpendingMap
//}

@Composable
fun CategorySpendingChart(categorySpendingMap: Map<String, Double>) {
    val categories = categorySpendingMap.keys.toList()
    val spendingValues = categorySpendingMap.values.map { it.toFloat() }

    BarChart(
        barChartData = BarChartData(
            bars = categories.mapIndexed { index, label ->
                val categoryInfo = label.split(" ") // Split the label to get name, icon, and color
                val categoryName = "${categoryInfo[0]} ${categoryInfo[1]}"
                Timber.d("${Color(categoryInfo[2].toLong())}")
                val categoryColor = Color(categoryInfo[2].toLong())

                BarChartData.Bar(
                    label = categoryName,
                    value = spendingValues[index],
                    color = categoryColor
                )
            }
        ),
        labelDrawer = CategoryLabelDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(
            labelTextColor = Color.White,
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
            Row {
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = billItem.name)
                    Text(text = billItem.quantity.toString())
                    Text(text = billItem.totalPrice.toString())

                }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = Shapes().large)
                        .weight(1f),
                    tonalElevation = Elevation.Level3
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "category"
                        )
                        Icon(
                            imageVector = Icons.Rounded.Build,
                            contentDescription = "category"
                        )
                        Icon(
                            imageVector = Icons.Rounded.MailOutline,
                            contentDescription = "category"
                        )
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "category"
                        )
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




