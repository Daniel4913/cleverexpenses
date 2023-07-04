package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.cleverex.displayable.ProductCategoryDisplayable
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.browseCategory.Category
import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.Name
import com.example.cleverex.presentation.components.CategoryAmount
import com.example.cleverex.ui.theme.Elevation
import timber.log.Timber
import java.time.ZonedDateTime


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewContent(
    selectedBill: Bill?,
    billItems: List<BillItem>,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    paddingValues: PaddingValues
) {
    // list of categories in bill

    val categoriesInBill = mutableListOf<Category>()
    billItems.forEach {
        categoriesInBill.add(
            Category(
                name = it.category.name,
                icon = it.category.icon,
                categoryColor = it.category.categoryColor
            )
        )
    }

    Timber.d("categoriesInBill ${categoriesInBill}")

    val categorizedProducts: Map<String, MutableList<BillItem>> = mutableMapOf()

    for (product in billItems) {
        val categoryName = product.category.name
        if (categorizedProducts.containsKey(categoryName.value)) {
            categorizedProducts[categoryName.value]?.add(product)
        } else {
            categorizedProducts[categoryName.value]?.add(product)
        }
    }


    for ((categoryName, product)in categorizedProducts) {

    }

    val productDisplayable = ProductCategoryDisplayable(
        name = "Dion Luna",
        icon = "fugit",
        color = 5219,
        quantity = 8055,
        unit = "habitant",
        price = 0.1,
        currency = "legimus"
    )

    Timber.d("categorized products: ${categorizedProducts}")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        var sum = 3.0
        items(items = categorizedProducts.toList()) {
            CategoryAmount(
                categoryColor = productDisplayable.color,
                name = productDisplayable.name,
                icon = productDisplayable.icon,
                quantity = productDisplayable.quantity,
                unit = productDisplayable.unit,
                price = productDisplayable.price
            )
        }

    }


//    billItems.forEach {
//        //TODO najpierw musze wyciagnac wszystkie kategorie jakie są przypisane do BillItem.
//        // Następnie stworzyć instancje dla kazdej wystepujacej kategorii w calym Bill.
//        // Potem musze dodac do kazdej istancji metodą copy powiedzmy i add wartości price, quanity itd
//        categoryNames.add(it.category.name)
//
//
//        displayableCategories.add(
//            ProductCategoryDisplayable(
//                name = it.category.name.value,
//                icon = it.name,
//                color = it.category.categoryColor.value,
//                quantity = 0,
//                unit = "",
//                price = 0.0,
//                currency = "todo: selectedBill.billCurrency"
//            )
//        )
//    }


//    val fakeItems = listOf(
//        ProductCategoryDisplayable(
//            name = "House",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 13,
//            unit = "pcs", price = 23.33
//        ),
//        ProductCategoryDisplayable(
//            name = "Dinner",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 783,
//            unit = "pcs", price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Toilet",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 5478,
//            unit = "pcs", price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Sweets",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 13,
//            unit = "pcs", price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Pierdoly jakies",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 1,
//            unit = "pcs", price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Street food",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 68497,
//            unit = "pcs", price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Costam costam",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 1,
//            unit = "", price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Work",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 25,
//            unit = "kg", price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Bardzo dluga nazwa kategorii",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 13,
//            unit = "pcs",
//            price = 193.23
//        ),
//        ProductCategoryDisplayable(
//            name = "Neighbour",
//            icon = Icons.Rounded.Home, color = Color.Green,
//            quantity = 133,
//            unit = "liters", price = 333.23
//        )
//    )

//    if (displayableCategories.isNotEmpty()) {
//        LazyVerticalStaggeredGrid(
//            columns = StaggeredGridCells.Fixed(2),
//            modifier = Modifier
//                .height(600.dp)
//                .navigationBarsPadding()
//                .padding(top = paddingValues.calculateTopPadding() + 50.dp),
//            contentPadding = PaddingValues(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            verticalItemSpacing = 10.dp,
//        ) {
//            items(items = displayableCategories) { item ->
//                CategoryAmount(
//                    name = item.name,
//                    icon = item.icon,
//                    quantity = item.quantity,
//                    unit = item.unit,
//                    price = item.price,
//                    categoryColor = item.color,
//                )
//            }
//
//        }
//    }
    Surface(
        modifier = Modifier
            .height(24.dp)
            .fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.2F)
    ) {
        Text(
            text = "Modifier" +
                    ".height(24.dp)" +
                    ".fillMaxWidth(),"
        )
    }
    if (billItems.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
        ) {
            // "pozbywamy sie ifów w tym całym CA" - i pewnie nullable - "?"
            items(
                items = billItems
            ) {
                Row {
                    Text(text = it.name)
                    Text(text = it.unit)
                    Text(text = it.price.toString())
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = Shapes().large),
                        tonalElevation = Elevation.Level3
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceAround) {
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
                Spacer(modifier = Modifier.height(12.dp))
            }

        }
    }

}

@Composable
fun GeneralCategoryContainer() {
    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color(0xff2589bd)),
        modifier = Modifier
            .width(width = 206.dp)
            .height(height = 52.dp)
            .clip(shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(width = 206.dp)
                .padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                )
        ) {
            Surface(
                shape = RoundedCornerShape(21.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .size(size = 40.dp)
                    .clip(shape = RoundedCornerShape(21.dp))
            ) {
                Image(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "spray-bottle",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier
                        .size(size = 24.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .width(width = 14.dp)
            )
            Text(
                text = "Electronics",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .weight(weight = 1f)
            )
            Spacer(
                modifier = Modifier
                    .width(width = 14.dp)
            )
            Text(
                text = "2352,99 $",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 12.sp
                )
            )
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .offset(
                    x = 196.dp,
                    y = (-12).dp
                )
                .size(size = 24.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(0xff2589bd))
        ) {
            Text(
                text = "1",
                color = Color.White,
                style = TextStyle(
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 9.dp,
                        y = 5.dp
                    )
            )
        }
    }
}


