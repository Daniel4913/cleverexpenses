//package com.example.cleverex.data
//
//import com.example.cleverex.domain.browseCategory.AllCategoriesDto
//import com.example.cleverex.domain.browseCategory.CategoryColor
//import com.example.cleverex.domain.browseCategory.CategoryDto
//import com.example.cleverex.domain.browseCategory.CategoryItem
//import com.example.cleverex.domain.browseCategory.Icon
//import com.example.cleverex.domain.browseCategory.Name
//
//class FakeCategoriesDb : CategoriesRepository {
//
//    override fun getCategories(): AllCategoriesDto {
//        val listDto = mutableListOf<CategoryDto>()
//        fakeCategories.forEach {
//            listDto.add(
//                CategoryDto(
//                    name = it.name,
//                    icon = it.icon,
//                    categoryColor = it.categoryColor
//                )
//            )
//        }
//        return AllCategoriesDto(
//            status = "brute",
//            totalCategories = fakeCategories.size,
//            categories = listDto
//        )
//    }
//
//    override fun getCategory(): CategoryItem {
//        TODO("Not yet implemented")
//    }
//
//    override fun insertCategory(): CategoryItem {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateCategory(): CategoryItem {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteCategory(): CategoryItem {
//        TODO("Not yet implemented")
//    }
//
//    private val fakeCategories =
//        listOf(
//            CategoryItem(
//                name = Name(value = "House"),
//                icon = Icon(value = "\uD83D\uDE07"),
//                categoryColor = CategoryColor(value = 0xFF6E5E00)
//            ),
//            CategoryItem(
//                name = Name(value = "Cleaning products"),
//                icon = Icon(value = "🧹"),
//                categoryColor = CategoryColor(value = 0xFF6E5E00)
//            ),
//            CategoryItem(
//                name = Name(value = "Food"),
//                icon = Icon(value = "\uD83D\uDE07"),
//                categoryColor = CategoryColor(value = 0xFF6E5E00)
//            ),
//            CategoryItem(
//                name = Name(value = "Street Food"),
//                icon = Icon(value = "\uD83D\uDE07"),
//                categoryColor = CategoryColor(value = 0xFF6E5E00)
//            ),
//            CategoryItem(
//                name = Name(value = "Electronics"),
//                icon = Icon(value = "\uD83D\uDE07"),
//                categoryColor = CategoryColor(value = 0xFF6E5E00)
//            ),
//            CategoryItem(
//                name = Name(value = "Restaurant"),
//                icon = Icon(value = "\uD83D\uDE07"),
//                categoryColor = CategoryColor(value = 0xFF6E5E00)
//            )
//        )
//}