package com.example.cleverex.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAmount(
    //todo source: Bill/ByCategoriesList/
    color: Color,
    name: String,
    icon: ImageVector,
    quantity: Int,
    unit: String,
    price: Double
) {
    BadgedBox(
        badge = {
            Badge(
                containerColor = color,
                modifier = Modifier.offset(
                    x = -calculateXOffset(quantity),
                    y = (5).dp
                )
            ) {
                Text(text = "$quantity")
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            border = BorderStroke(width = 1.dp, color = color),
            shape = Shapes().medium,
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                Icon(imageVector = icon, contentDescription = "Category Icon")
                Text(text = name, modifier = Modifier.weight(1f))
                Text(
                    text = "$price",
                )
            }
        }

    }
}

fun calculateXOffset(quantity: Int): Dp {
    var offset = 0
    when ("$quantity".length) {
        1 -> offset = 6
        2 -> offset = 12
        3 -> offset = 18
        4 -> offset = 26
        5 -> offset = 34
    }

    return offset.dp
}

@Preview
@Composable
fun CategoryAmountPreview() {
    CategoryAmount(
        color = Color.Blue,
        name = "Na obiad",
        icon = Icons.Rounded.MailOutline,
        quantity = 15,
        unit = "szt.",
        price = 172.29
    )
}