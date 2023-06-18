package com.example.cleverex.presentation.displayable

import com.example.cleverex.domain.Mapper
import com.example.cleverex.model.Bill

class BillToDisplayableMapper : Mapper<Bill, BillDisplayable> {
    override fun map(from: Bill): BillDisplayable {
        return BillDisplayable(
            shop = from.shop,
            address = from.address ?: "",
            billDate = from.billDate, price = from.price,
            billItems = from.billItems,
            billImage = from.billImage ?: ""
        )
    }
}

