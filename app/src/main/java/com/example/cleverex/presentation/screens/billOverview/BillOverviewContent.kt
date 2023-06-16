package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

import com.example.cleverex.model.Bill
import com.example.cleverex.presentation.components.CategoryAmount
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
        CategoriesFromProducts("House", Icons.Rounded.Home, 13, "pcs", price = 23.33),
        CategoriesFromProducts("Dinner", Icons.Rounded.Home, 783, "pcs", price = 193.23),
        CategoriesFromProducts("Toilet", Icons.Rounded.Home, 5478, "pcs", price = 193.23),
        CategoriesFromProducts("Sweets", Icons.Rounded.Home, 13, "pcs", price = 193.23),
        CategoriesFromProducts("Pierdoly jakies", Icons.Rounded.Home, 1, "pcs", price = 193.23),
        CategoriesFromProducts("Street food", Icons.Rounded.Home, 68497, "pcs", price = 193.23),
        CategoriesFromProducts("Costam costam", Icons.Rounded.Home, 1, "", price = 193.23),
        CategoriesFromProducts("Work", Icons.Rounded.Home, 25, "kg", price = 193.23),
        CategoriesFromProducts(
            "Bardzo dluga nazwa kategorii",
            Icons.Rounded.Home,
            13,
            "pcs",
            price = 193.23
        ),
        CategoriesFromProducts("Neighbour", Icons.Rounded.Home, 133, "liters", price = 333.23)
    )

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding() + 50.dp),
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
                color = Color.Magenta
            )
        }
    }

    
    //
}

data class CategoriesFromProducts(
    val name: String,
    val icon: ImageVector,
    val quantity: Int,
    val unit: String,
    val price: Double
)


