package com.example.cleverex.presentation.screens.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleverex.displayable.CategoryDisplayable
import com.example.cleverex.model.CategoryColor
import com.example.cleverex.model.CategoryItem
import com.example.cleverex.model.Icon
import com.example.cleverex.model.Name
import com.example.cleverex.ui.theme.Elevation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    categories: List<CategoryDisplayable>,
    onCategoryPressed: () -> Unit,
    onBackPressed: () -> Unit
) {
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
        CategoriesContent(categories = categories)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    categories: List<CategoryDisplayable>
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
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
        tonalElevation = Elevation.Level3,
        color = color.value
    ) {
        Text(text = name.value)
        Icon(
            imageVector = icon.value,
            contentDescription = "Category Icon"
        )
    }
}