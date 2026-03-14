package com.markdownreader.di

import android.content.Context
import com.markdownreader.data.datasource.FileDataSource
import com.markdownreader.data.datasource.RecentFilesDataSource
import com.markdownreader.data.datasource.ThemeDataSource
import com.markdownreader.data.repository.FileRepositoryImpl
import com.markdownreader.data.repository.RecentFilesRepositoryImpl
import com.markdownreader.data.repository.ThemeRepositoryImpl
import com.markdownreader.domain.repository.FileRepository
import com.markdownreader.domain.repository.RecentFilesRepository
import com.markdownreader.domain.repository.ThemeRepository
import com.markdownreader.domain.usecase.LoadMarkdownFileUseCase
import com.markdownreader.domain.usecase.ParseMarkdownUseCase
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin

class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val markwonInstance: Markwon = Markwon.builder(appContext)
        .usePlugin(CoilImagesPlugin.create(appContext))
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(TablePlugin.create(appContext))
        .usePlugin(HtmlPlugin.create())
        .usePlugin(TaskListPlugin.create(appContext))
        .build()

    private val fileDataSource = FileDataSource(appContext)
    private val themeDataSource = ThemeDataSource(appContext)
    private val recentFilesDataSource = RecentFilesDataSource(appContext)

    private val fileRepository: FileRepository = FileRepositoryImpl(fileDataSource)
    val themeRepository: ThemeRepository = ThemeRepositoryImpl(themeDataSource)
    val recentFilesRepository: RecentFilesRepository = RecentFilesRepositoryImpl(recentFilesDataSource)

    val loadMarkdownFileUseCase = LoadMarkdownFileUseCase(fileRepository)
    val parseMarkdownUseCase = ParseMarkdownUseCase(markwonInstance)
}
