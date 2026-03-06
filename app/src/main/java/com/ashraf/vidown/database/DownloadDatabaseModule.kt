package com.ashraf.vidown.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DownloadDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) : DownloadDatabase =
        Room.databaseBuilder(
            context,
            DownloadDatabase::class.java,
            "download_db"
        )
    .build()

    @Provides
    fun provideDownloadDao( db : DownloadDatabase) : DownloadDao = db.downloadsDao()
}