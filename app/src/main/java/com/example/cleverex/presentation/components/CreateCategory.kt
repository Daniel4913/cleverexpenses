import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cleverex.presentation.screens.budget.UiState
import com.example.cleverex.presentation.screens.categories.CategoriesState
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateCategory(
    uiState: CategoriesState,
    onCreateClicked: () -> Unit,
    paddingValues: PaddingValues,
    showColorPicker: (Boolean) -> Unit,
    onNameChanged: (String) -> Unit,
    onIconChanged: (String) -> Unit,
    onColorChanged: (Color) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val colorPickerController = rememberColorPickerController()
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 8.dp)
            .padding(horizontal = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Surface(
                color = uiState.newCategoryColor,
                modifier = Modifier
                    .size(width = 24.dp, height = componentHeight - 8.dp)
                    .clip(Shapes().large),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                onClick = {
                    showColorPicker(true)
                },
            ) {}
            OutlinedTextField(
                value = uiState.newCategoryIcon,
                onValueChange = onIconChanged,
                label = { Text(text = "🛸Icon") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned {
                        componentHeight = with(localDensity) { it.size.height.toDp() }
                    }
            )
            OutlinedTextField(
                value = uiState.newCategoryName,
                onValueChange = onNameChanged,
                label = { Text(text = "Category Name") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .weight(3f)
            )

            if (uiState.colorPickerShowing) {
                Dialog(onDismissRequest = { showColorPicker(false) }) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = Shapes().extraLarge
                    ) {
                        Column(
                            modifier = Modifier.padding(all = 30.dp)
                        ) {
                            Text(
                                text = "Select a color",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AlphaTile(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    controller = colorPickerController
                                )
                            }
                            HsvColorPicker(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(10.dp),
                                controller = colorPickerController,
                                onColorChanged = { envelope ->
                                    onColorChanged(envelope.color)
                                },
                            )
                            TextButton(
                                onClick = { showColorPicker(false) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                            ) {
                                Text("Done")
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                onCreateClicked()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Create Category")
        }
    }
}


