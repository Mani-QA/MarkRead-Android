# Markdown Reader

A high-performance Android Markdown reader built with Kotlin and Jetpack Compose. Opens `.md` files from device storage, renders them with full formatting support, and provides a comfortable reading experience across three distinct visual themes.

## Features

- **Markdown Rendering** - Full GFM (GitHub Flavored Markdown) support including headings, bold, italic, strikethrough, tables, task lists, inline HTML, code blocks, links, and images.
- **Image Loading** - Remote and local images rendered inline via Coil with caching and memory management.
- **Three Theme Modes** - Light, Dark, and Sepia. Sepia uses `#F4ECD8` background / `#5B4636` text for a warm, paper-like reading experience.
- **Persistent Preferences** - Theme choice saved via DataStore and restored on app restart.
- **SAF File Access** - Uses Android's Storage Access Framework for secure, permission-aware file picking with persistable URI grants.
- **Edge Case Handling** - Graceful UI states for empty files, oversized files (>10 MB cap), permission errors, and general I/O failures.
- **Background Parsing** - Markdown is parsed on `Dispatchers.Default` via Coroutines so the UI thread stays free during scroll.
- **Intent Handling** - Registers as a viewer for `text/markdown` MIME types so other apps can open `.md` files directly.

## Architecture

The project follows **Clean Architecture** with three distinct layers:

```
┌──────────────────────────────────────────┐
│  UI Layer (Jetpack Compose + ViewModel)  │
│  - ReaderScreen, ThemeMenu, States       │
│  - ReaderViewModel                       │
├──────────────────────────────────────────┤
│  Domain Layer (Pure Kotlin)              │
│  - Models: AppTheme, MarkdownDocument    │
│  - Repositories: FileRepository,         │
│    ThemeRepository (interfaces)          │
│  - Use Cases: LoadMarkdownFileUseCase,   │
│    ParseMarkdownUseCase                  │
├──────────────────────────────────────────┤
│  Data Layer (Android / IO)               │
│  - FileDataSource (SAF via ContentResolver)│
│  - ThemeDataSource (DataStore Preferences)│
│  - Repository implementations            │
└──────────────────────────────────────────┘
```

### Key Design Decisions

| Concern | Approach |
|---|---|
| Markdown engine | Markwon 4.6 with plugins (tables, strikethrough, task lists, HTML, Coil images) |
| Image loading | Coil 2.x integrated with Markwon's image plugin |
| Theme persistence | DataStore Preferences with Flow-based observation |
| File access | Storage Access Framework with `OpenDocument` contract |
| DI | Manual constructor injection via `AppContainer` (no framework overhead) |
| Threading | `Dispatchers.IO` for file reads, `Dispatchers.Default` for Markdown parsing |

## Tech Stack

| Component | Library |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Markdown | Markwon 4.6.2 |
| Images | Coil 2.7.0 |
| Persistence | DataStore Preferences 1.1.1 |
| Async | Kotlin Coroutines 1.9.0 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 (Android 15) |

## Project Structure

```
app/src/main/java/com/markdownreader/
├── di/                  # Dependency injection container
├── domain/
│   ├── model/           # AppTheme, MarkdownDocument, FileLoadResult
│   ├── repository/      # FileRepository, ThemeRepository interfaces
│   └── usecase/         # LoadMarkdownFileUseCase, ParseMarkdownUseCase
├── data/
│   ├── datasource/      # FileDataSource (SAF), ThemeDataSource (DataStore)
│   └── repository/      # FileRepositoryImpl, ThemeRepositoryImpl
├── ui/
│   ├── theme/           # Color, Theme, Typography definitions
│   ├── components/      # MarkdownContent, ThemeMenu, EmptyState
│   ├── screen/          # ReaderScreen
│   └── viewmodel/       # ReaderViewModel, ReaderViewModelFactory
├── MainActivity.kt
└── MarkdownReaderApp.kt
```

## Building

Open the project in Android Studio and sync Gradle, then run on a device or emulator:

```bash
./gradlew assembleDebug
```

## Security

- No API keys or secrets are used in this project.
- File access is scoped through SAF with explicit user consent per file.
- Persistable URI permissions are acquired so reopening works without re-prompting.
