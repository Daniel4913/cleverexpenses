package com.example.cleverex.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.cleverex.util.calculateContentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAmount(
    //todo source: Bill/ByCategoriesList/
    categoryColor: Long,
    name: String,
    icon: String,
    quantity: Int,
    unit: String,
    currency: String = "zł",
    price: Double
) {
    val cardBackground = CardDefaults.cardColors(
        containerColor = Color.Transparent
    )
    BadgedBox(
        badge = {
            Badge(
                containerColor = Color(categoryColor),
                contentColor = calculateContentColor(Color(categoryColor)),
                modifier = Modifier.offset(
                    x = -mapXOffset(quantity),
                    y = (5).dp
                )
            ) {
                Text(text = "$quantity")
            }
        }
    ) {
        Card(
            colors = cardBackground,
            modifier = Modifier.fillMaxSize(),
            border = BorderStroke(width = 1.dp, color = Color(categoryColor).copy(alpha = 0.8F)),
            shape = Shapes().medium,
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly // nie dziala arrangement
            ) {

                Text(text = icon)
                Text(
                    text = name,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                )
                Text(
                    text = "$price",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                )
                Text(
                    text = currency,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                )
            }
        }

    }
}

fun calculateXOffset(quantity: Int): Dp {
    var offset = 0
    when ("$quantity".length) {
        1 -> offset = 7
        2 -> offset = 12
        3 -> offset = 18
        4 -> offset = 26
        5 -> offset = 34
    }

    return offset.dp
}

// propozycja chata o usprawnienie funkcji:
// "To usprawnienie pozwoli na łatwe dodawanie nowych długości tekstu i
// odpowiadających im wartości offsetu, bez konieczności
// ręcznej modyfikacji instrukcji warunkowych."
fun mapXOffset(quantity: Int): Dp {
    val offsetMap = mapOf(
        1 to 5,
        2 to 12,
        3 to 18,
        4 to 26,
        5 to 34
    )

    val length = "$quantity".length
    val offset = offsetMap[length] ?: 0

    return offset.dp
}
// lepsza implementacja?

