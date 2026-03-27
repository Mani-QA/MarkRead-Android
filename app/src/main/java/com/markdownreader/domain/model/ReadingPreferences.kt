package com.markdownreader.domain.model

data class ReadingPreferences(
    val fontScale: Float = 1.0f,
    val lineHeightMultiplier: Float = 1.4f
) {
    companion object {
        const val MIN_FONT_SCALE = 0.7f
        const val MAX_FONT_SCALE = 2.0f
        const val FONT_SCALE_STEP = 0.1f

        const val MIN_LINE_HEIGHT = 1.0f
        const val MAX_LINE_HEIGHT = 2.2f
        const val LINE_HEIGHT_STEP = 0.1f
    }
}
