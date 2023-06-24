package com.example.cleverex.displayable

import java.time.Instant

data class BillPreviewDisplayable(
    val shop: String,
    val date: Instant,
    val price: Double,
    val hasImage: Boolean
)