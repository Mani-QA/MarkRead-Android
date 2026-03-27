# MarkRead

A lightweight, high-performance Markdown reader for Android built with Kotlin and Jetpack Compose. Opens `.md` files from device storage, renders them natively in Compose, and provides a comfortable reading experience with Material You theming and three distinct visual modes.

## Features

- **Native Compose Markdown** — Full GFM rendering (headings, bold, italic, strikethrough, tables, task lists, code blocks, links, images) using a pure Compose renderer — no WebView or legacy TextView.
- **Image Loading** — Remote and local images rendered inline via Coil with caching and memory management.
- **Material You + Three Theme Modes** — Light and Dark themes use dynamic color extraction from your wallpaper on Android 12+. Sepia mode uses `#F4ECD8` background / `#5B4636` text for a warm, paper-like reading experience.
- **Recent Files** — Quick access to previously opened files from the home screen, backed by a Room database.
- **Read Position Memory** — Scroll position is saved per file and restored when you reopen it.
- **Persistent Preferences** — Theme choice saved via DataStore and restored on app restart.
- **File Association** — Registers as a handler for `.md` files across all Android file managers, browsers, and share sheets.
- **SAF File Access** — Uses Android's Storage Access Framework for secure, permission-aware file picking with persistable URI grants.
- **Edge Case Handling** — Graceful UI states for empty files, oversized files (>10 MB cap), permission errors, and general I/O failures.
- **Background Processing** — File I/O runs on `Dispatchers.IO` via Coroutines so the UI thread stays free.
- **About Dialog** — App branding with version info and author link.

## Architecture

The project follows **Clean Architecture** with Dagger Hilt for dependency injection:

```
┌──────────────────────────────────────────────┐
│  UI Layer (Jetpack Compose + ViewModel)      │
│  - ReaderScreen, ThemeMenu, AboutDialog      │
│  - ReaderViewModel (@HiltViewModel)          │
│  - Sealed interface ReaderUiState            │
├──────────────────────────────────────────────┤
│  Domain Layer (Pure Kotlin)                  │
│  - Models: AppTheme, MarkdownDocument,       │
│    RecentFile, ReadPosition, FileLoadResult  │
│  - Repositories (interfaces)                 │
│  - Use Cases: LoadMarkdownFileUseCase        │
├──────────────────────────────────────────────┤
│  Data Layer (Android / IO)                   │
│  - Room DB: RecentFileDao, ReadPositionDao   │
│  - FileDataSource (SAF via ContentResolver)  │
│  - ThemeDataSource (DataStore Preferences)   │
│  - Repository implementations                │
├──────────────────────────────────────────────┤
│  DI Layer (Dagger Hilt)                      │
│  - DatabaseModule, DataModule                │
│  - @HiltAndroidApp, @AndroidEntryPoint       │
└──────────────────────────────────────────────┘
```

### Key Design Decisions

| Concern | Approach |
|---|---|
| Markdown engine | multiplatform-markdown-renderer-m3 (pure Compose, Material 3 native) |
| Image loading | Coil 2.x with Compose integration |
| Theme | Material You dynamic colors (Android 12+) with static fallback + Sepia |
| State management | Exhaustive sealed interface (`ReaderUiState`) for compile-time safety |
| Persistence | Room for recent files & read positions; DataStore for theme |
| File access | Storage Access Framework with `OpenDocument` contract |
| DI | Dagger Hilt with `@HiltViewModel`, `@AndroidEntryPoint` |
| Threading | `Dispatchers.IO` for file reads, Coroutines throughout |

## Tech Stack

| Component | Library |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 + Material You |
| Markdown | multiplatform-markdown-renderer 0.27.0 |
| Images | Coil 2.7.0 |
| DI | Dagger Hilt 2.53.1 |
| Database | Room 2.6.1 |
| Preferences | DataStore Preferences 1.1.1 |
| Async | Kotlin Coroutines 1.9.0 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 (Android 15) |

## Project Structure

```
app/src/main/java/com/markdownreader/
├── di/                      # Hilt modules (DatabaseModule, DataModule)
├── domain/
│   ├── model/               # AppTheme, MarkdownDocument, RecentFile,
│   │                        # ReadPosition, FileLoadResult (sealed interface)
│   ├── repository/          # FileRepository, ThemeRepository,
│   │                        # RecentFilesRepository, ReadPositionRepository
│   └── usecase/             # LoadMarkdownFileUseCase
├── data/
│   ├── local/               # Room database, entities, DAOs
│   ├── datasource/          # FileDataSource (SAF), ThemeDataSource (DataStore)
│   └── repository/          # All repository implementations
├── ui/
│   ├── theme/               # Color, Theme (Material You), Typography
│   ├── components/          # MarkdownContent, ThemeMenu, RecentFilesList,
│   │                        # AboutDialog, EmptyState
│   ├── screen/              # ReaderScreen
│   └── viewmodel/           # ReaderViewModel (sealed interface state)
├── MainActivity.kt          # @AndroidEntryPoint
└── MarkdownReaderApp.kt     # @HiltAndroidApp
```

## Building

```bash
# Debug APK
./gradlew assembleDebug

# Release AAB (for Play Store)
./gradlew bundleRelease
```

## Security

- No API keys or secrets are used in this project.
- File access is scoped through SAF with explicit user consent per file.
- Persistable URI permissions are acquired so reopening works without re-prompting.
- Keystore files (`.jks`) are excluded from version control via `.gitignore`.
