package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import com.example.cleverex.model.Bill
import com.example.cleverex.model.BillItem
import com.example.cleverex.presentation.components.CategoryAmount
import com.example.cleverex.ui.theme.Elevation
import java.time.ZonedDateTime


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewContent(
    selectedBill: Bill?,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit,
    paddingValues: PaddingValues
) {
    val items = listOf(
        CategoriesFromProducts(
            "House",
            Icons.Rounded.Home,
            color = Color.Blue,
            13,
            "pcs",
            price = 23.33
        ),
        CategoriesFromProducts(
            "Dinner",
            Icons.Rounded.Home,
            color = Color.Magenta,
            783,
            "pcs",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Toilet",
            Icons.Rounded.Home,
            color = Color.Green,
            5478,
            "pcs",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Sweets",
            Icons.Rounded.Home,
            color = Color.Cyan,
            13,
            "pcs",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Pierdoly jakies",
            Icons.Rounded.Home,
            color = Color.LightGray,
            1,
            "pcs",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Street food",
            Icons.Rounded.Home,
            color = Color.Magenta,
            68497,
            "pcs",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Costam costam",
            Icons.Rounded.Home,
            color = Color.Red,
            1,
            "",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Work",
            Icons.Rounded.Home,
            color = Color.Yellow,
            25,
            "kg",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Bardzo dluga nazwa kategorii",
            Icons.Rounded.Home,
            color = Color.Green,
            13,
            "pcs",
            price = 193.23
        ),
        CategoriesFromProducts(
            "Neighbour",
            Icons.Rounded.Home,
            color = Color.Blue,
            133,
            "liters",
            price = 333.23
        )
    )

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding()),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 10.dp,
    ) {
        items(items) { item ->
            CategoryAmount(
                name = item.name,
                icon = item.icon,
                quantity = item.quantity,
                unit = item.unit,
                price = item.price,
                categoryColor = item.color,
            )
        }
    }
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 14.dp)
    ) {
        // "pozbywamy sie ifów w tym całym CA" - i pewnie nullable - "?"
        val billsItems = selectedBill?.billItems

        billsItems.let {
            if (it != null) {
                items(
                    items = it.toList()
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


