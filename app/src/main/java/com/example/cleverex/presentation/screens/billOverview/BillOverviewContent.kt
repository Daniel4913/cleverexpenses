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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.presentation.components.CategoryAmount
import com.example.cleverex.ui.theme.Elevation
import timber.log.Timber
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewContent(
    selectedBill: Bill?,
    billItems: List<BillItem>,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit,
    paddingValues: PaddingValues
) {
    if (billItems.isNotEmpty()) {
        Timber.d("billItems: ${billItems.size}")
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
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


data class CategoriesFromProducts(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val quantity: Int,
    val unit: String,
    val price: Double,
    val currency: String = "zł"
)




