package com.example.cleverex.domain.browseCategory


//todo All categories poniewaz pozniej bym chcial aby byly podkategorie
data class AllCategoriesDto(
 val status: String,
 val totalCategories: Int, // kategorie i subkategorie razem
 val categories: List<CategoryDto>
)




