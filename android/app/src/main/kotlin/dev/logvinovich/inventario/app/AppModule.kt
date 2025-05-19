package dev.logvinovich.inventario.app

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.logvinovich.inventario.data.di.DataModule
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule::class])
object AppModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context) =
        PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile(PREFERENCES_STORE)
            }
        )

    private const val PREFERENCES_STORE = "prefs"
}