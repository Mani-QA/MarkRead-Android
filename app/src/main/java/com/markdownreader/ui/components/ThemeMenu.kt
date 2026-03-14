package com.markdownreader.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.markdownreader.domain.model.AppTheme

@Composable
fun ThemeMenu(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Outlined.Palette,
            contentDescription = "Change theme"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        AppTheme.entries.forEach { theme ->
            DropdownMenuItem(
                text = { Text(theme.displayName) },
                onClick = {
                    onThemeSelected(theme)
                    expanded = false
                },
                leadingIcon = {
                    RadioButton(
                        selected = currentTheme == theme,
                        onClick = null
                    )
                }
            )
        }
    }
}
