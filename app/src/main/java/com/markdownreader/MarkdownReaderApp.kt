package com.markdownreader

import android.app.Application
import com.markdownreader.di.AppContainer

class MarkdownReaderApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
