# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ComponentSupplier { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# Compose Markdown
-keep class com.mikepenz.markdown.** { *; }
-dontwarn com.mikepenz.markdown.**
-keep class org.commonmark.** { *; }
-dontwarn org.commonmark.**
