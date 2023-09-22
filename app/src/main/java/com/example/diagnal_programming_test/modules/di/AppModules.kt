package com.example.diagnal_programming_test.modules.di

import android.content.Context
import com.example.diagnal_programming_test.DiagnalApp
import com.example.diagnal_programming_test.data.adapter.DiagnalRecyclerAdapter
import com.example.diagnal_programming_test.data.repo.DiagnalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    /** App Modules */
    @Singleton
    @Provides
    fun provideDiagnalApplication(): DiagnalApp {
        return DiagnalApp()
    }

    /** Repository Modules */
    @Singleton
    @Provides
    fun provideRepository(@ApplicationContext context: Context): DiagnalRepository {
        return DiagnalRepository(context)
    }

    @Singleton
    @Provides
    fun provideRecyclerAdapter(@ApplicationContext context: Context): DiagnalRecyclerAdapter {
        return DiagnalRecyclerAdapter(context)
    }
}