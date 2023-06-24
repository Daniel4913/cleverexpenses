package com.example.cleverex.presentation.screens.categories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.Name
import com.example.cleverex.ui.theme.Elevation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseCategoriesScreen(
    categories: List<CategoryDisplayable>,
    onCategoryPressed: () -> Unit,
    onBackPressed: () -> Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Navigate back icon"
                        )
                    }
                },
                title = { Text(text = "BrowseCategories") },
                actions = {
                    IconButton(onClick = {
                    }) {
                        Icons.Rounded.Build
                    }
                }
            )
        }) {
        padding = it

        CategoriesContent(
            categories = categories,
            paddingValues = padding
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    categories: List<CategoryDisplayable>,
    paddingValues: PaddingValues
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding()),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(categories) { categoryItem ->
            CategoryOverview(
                categoryItem.name,
                icon = Icon(value = categoryItem.icon.value),
                color = CategoryColor(value = categoryItem.categoryColor.value)
            )
        }
    }
}

@Composable
fun CategoryOverview(name: Name, icon: Icon, color: CategoryColor) {
    Surface(
        modifier = Modifier.size(100.dp),
        tonalElevation = Elevation.Level1,
        color = Color(color.value)
    ) {
        Text(text = name.value)
        CategoryIcon(emoji = icon.value)
    }
}

@Composable
fun CategoryIcon(emoji: String) {
    Text(text = emoji, style = TextStyle(fontSize = MaterialTheme.typography.titleMedium.fontSize))
}