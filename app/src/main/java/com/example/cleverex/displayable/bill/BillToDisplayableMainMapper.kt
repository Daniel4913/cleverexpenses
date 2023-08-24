package com.example.cleverex.displayable.bill

import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.browseCategory.MainMapper

class BillToDisplayableMainMapper : MainMapper<Bill, BillDisplayable> {
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