package com.senijoshua.swish.di

import com.senijoshua.swish.data.DefaultMainRepository
import com.senijoshua.swish.data.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun provideMainRepositoryImplementation(repository: DefaultMainRepository): MainRepository
}
