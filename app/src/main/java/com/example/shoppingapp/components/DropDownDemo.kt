package com.example.shoppingapp.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.shoppingapp.Category
import com.example.shoppingapp.R


@Composable
fun DropdownDemo(
    selectedText: MutableState<String>
) {

    var expanded by remember { mutableStateOf(false) }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (expanded)
        R.drawable.icon_done
    else
        R.drawable.icon_edit

    Column(Modifier.padding(20.dp)) {

        // Create an Outlined Text Field
        // with icon and not expanded
        OutlinedTextField(
            value = selectedText.value,
            onValueChange = { selectedText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text("Label") },
            trailingIcon = {
                Icon(
                    painterResource(icon),
                    "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            enabled = false
        )

        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            for (category in Category.entries) {
                val label = category.enam
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        selectedText.value = label
                        expanded = false
                    }
                )
            }
        }

    }

}