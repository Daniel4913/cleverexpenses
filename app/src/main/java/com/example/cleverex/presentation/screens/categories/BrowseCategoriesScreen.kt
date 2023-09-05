package com.example.cleverex.presentation.screens.categories

import CreateCategory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.cleverex.R
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.Name
import com.example.cleverex.ui.theme.Elevation
import kotlinx.coroutines.flow.StateFlow
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseCategoriesScreen(
    uiState: StateFlow<CategoriesState>,
    onCategoryPressed: () -> Unit,
    onBackPressed: () -> Unit,
    showColorPicker: (Boolean) -> Unit,
    onNameChanged: (String) -> Unit,
    onIconChanged: (String) -> Unit,
    onColorChanged: (Color) -> Unit,
    onCreateClicked: () -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    val state = uiState.collectAsState()

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
        Column(verticalArrangement = Arrangement.SpaceAround) {
            CreateCategory(
                uiState = state.value,
                onCreateClicked = onCreateClicked,
                paddingValues = padding,
                showColorPicker = { showColorPicker(it) },
                onNameChanged = onNameChanged,
                onIconChanged = onIconChanged,
                onColorChanged = onColorChanged
            )
            CategoriesContent(
                uiState = uiState
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    uiState: StateFlow<CategoriesState>
) {
    val state = uiState.collectAsState()
    val categories = state.value.categories
    AnimatedVisibility(visible = true) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            itemsIndexed(
                items = categories,
                key = { _: Int, category: CategoryDisplayable ->
//                    java.lang.IllegalArgumentException: Key "D" was already used. If you are using LazyColumn/Row please make sure you provide a unique key for each item.
                    category.name.value
                }
            ) { index, category ->
                SwipeableActionsBox(
                    endActions = listOf(
                        SwipeAction(
                            icon = painterResource(id = R.drawable.ai_color),
                            background = Color.Red,
                            onSwipe = {}
                        ),
                    ),
                    modifier = Modifier
                        .animateItemPlacement()
                        .clip(shape = Shapes().medium)) {
                    CategoryOverview(
                        name = category.name,
                        icon = category.icon,
                        color = category.categoryColor
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
//                if (index < categories.size - 1) {
//                    Row(
//                        modifier = Modifier
//                            .background(Color.Green)
//                            .height(1.dp)
//                    ) {
//                        Divider(
//                            modifier = Modifier.padding(start = 16.dp),
//                            thickness = 1.dp,
//                            color = Color.Blue
//                        )
//                    }
//                }
            }
        }
    }
}

@Composable
fun CategoryOverview(name: Name, icon: Icon, color: CategoryColor) {
    val uLongColor = Color(color.value.toULong())
    Row {
        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .fillMaxWidth()
                .height(50.dp),
            tonalElevation = Elevation.Level1
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = uLongColor,
                    modifier = Modifier.size(width = 24.dp, height = 24.dp)
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
                CategoryIcon(emoji = icon.value)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = name.value)
            }
        }
    }

}

@Composable
fun CategoryIcon(emoji: String) {
    Text(text = emoji, style = TextStyle(fontSize = MaterialTheme.typography.titleMedium.fontSize))
}