package com.example.cleverex.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.cleverex.domain.BillItem
import com.example.cleverex.presentation.screens.addBill.BillItemDisplayable
import com.example.cleverex.ui.theme.Elevation

@Composable
fun ItemOverviewDispl(billItem: BillItemDisplayable) {
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
                                text = billItem.categories[0].icon.value,
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