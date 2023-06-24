package com.example.cleverex.displayable.bill

import java.time.Instant

data class BillPreviewDisplayable(
    val shop: String,
    val date: Instant,
    val price: Double,
    val hasImage: Boolean
)