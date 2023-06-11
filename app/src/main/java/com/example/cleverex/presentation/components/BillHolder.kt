package com.example.cleverex.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cleverex.R
import com.example.cleverex.model.Bill
import com.example.cleverex.ui.theme.Elevation
import com.example.cleverex.util.toInstant
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Composable
fun BillHolder(bill: Bill, onClick: (String) -> Unit) {
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }

    Row(modifier = Modifier.clickable(
        // disable ripple effect
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        }
    ) {
        onClick(bill._id.toHexString())
    })
    {
        Spacer(modifier = Modifier.width(29.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            modifier = Modifier
                .clip(
                    shape = Shapes().medium
                )
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.Level1
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp)
            ) {
                BillContent(
                    shop = bill.shop,
                    date = bill.billDate.toInstant(),
                    price = bill.price,
                    hasImage = bill.billImage?.isNotEmpty() ?: false
                )
            }
        }
    }
}

@Composable
fun BillContent(
    shop: String,
    date: Instant,
    price: Double,
    hasImage: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = shop,
                color = MaterialTheme.colorScheme.inversePrimary,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
            )
            Text(
                text = SimpleDateFormat("dd E HH:mm", Locale.ROOT).format(Date.from(date)),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasImage) {
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .alpha(0.7f),
                    painter = painterResource(id = R.drawable.ic_bill),
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = "Bill icon"
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clip(shape = Shapes().medium),
                ) {
                    Text(
                        text = "$price $",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BillHolderPreview() {
    BillHolder(bill = Bill().apply {
        shop = "Lidl"
        price = 1825.85
    }, onClick = {})
}

@Preview(showBackground = true)
@Composable
fun BillContentPreview() {
    BillContent(
        shop = "Lidl",
        date = Instant.now(),
        price = 55.78,
        hasImage = false
    )
}