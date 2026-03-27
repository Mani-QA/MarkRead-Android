package com.markdownreader

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markdownreader.ui.screen.ReaderScreen
import com.markdownreader.ui.theme.MarkdownReaderTheme
import com.markdownreader.ui.viewmodel.ReaderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ReaderViewModel by viewModels()

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { selectedUri ->
            try {
                contentResolver.takePersistableUriPermission(
                    selectedUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Some providers don't support persistable permissions
            }
            viewModel.loadFile(selectedUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIncomingIntent()

        val versionName = packageManager.getPackageInfo(packageName, 0).versionName ?: "1.0.0"

        setContent {
            val theme by viewModel.currentTheme.collectAsState()

            MarkdownReaderTheme(appTheme = theme) {
                ReaderScreen(
                    viewModel = viewModel,
                    versionName = versionName,
                    onOpenFile = { openFilePicker() }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent()
    }

    private fun openFilePicker() {
        filePickerLauncher.launch(arrayOf("text/markdown", "text/plain", "text/x-markdown"))
    }

    private fun handleIncomingIntent() {
        val action = intent?.action
        if (action == Intent.ACTION_VIEW || action == Intent.ACTION_SEND) {
            intent.data?.let { uri ->
                viewModel.loadFile(uri)
            }
        }
    }
}
