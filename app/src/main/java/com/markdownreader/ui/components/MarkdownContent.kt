package com.markdownreader.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.markdownreader.domain.model.ReadingPreferences

@Composable
fun MarkdownContent(
    rawMarkdown: String,
    initialScrollOffset: Int,
    onScrollPositionChanged: (Int) -> Unit,
    readingPreferences: ReadingPreferences,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState(initial = initialScrollOffset)

    LaunchedEffect(initialScrollOffset) {
        if (initialScrollOffset > 0) {
            scrollState.scrollTo(initialScrollOffset)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onScrollPositionChanged(scrollState.value)
        }
    }

    val scale = readingPreferences.fontScale
    val lineMultiplier = readingPreferences.lineHeightMultiplier

    val textColor = MaterialTheme.colorScheme.onBackground
    val codeTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    fun TextStyle.scaled(): TextStyle = copy(
        fontSize = fontSize * scale,
        lineHeight = fontSize * scale * lineMultiplier,
        color = textColor
    )

    Markdown(
        content = rawMarkdown,
        colors = markdownColor(
            codeBackground = MaterialTheme.colorScheme.surfaceVariant,
            dividerColor = MaterialTheme.colorScheme.outlineVariant
        ),
        typography = markdownTypography(
            h1 = MaterialTheme.typography.headlineLarge.scaled(),
            h2 = MaterialTheme.typography.headlineMedium.scaled(),
            h3 = MaterialTheme.typography.titleLarge.scaled(),
            h4 = MaterialTheme.typography.titleMedium.scaled(),
            h5 = MaterialTheme.typography.titleSmall.scaled(),
            h6 = MaterialTheme.typography.labelLarge.scaled(),
            text = MaterialTheme.typography.bodyLarge.scaled(),
            paragraph = MaterialTheme.typography.bodyMedium.scaled(),
            code = MaterialTheme.typography.bodySmall.copy(
                fontSize = MaterialTheme.typography.bodySmall.fontSize * scale,
                color = codeTextColor
            )
        ),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(
                top = 12.dp,
                bottom = 12.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    )
}
