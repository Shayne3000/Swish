package com.senijoshua.swish.shared_test

import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.di.DataModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * This replaces the production DataModule with a FakeDataModule
 * in all test classes in the project.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
abstract class FakeDataModule {

    /**
     * Provisions [FakeMainRepository] as the implementation of the MainRepository interface/binding
     * for all test classes in the project
     */
    @Singleton
    @Binds
    abstract fun provideMainRepositoryFakeImplementation(repository: FakeMainRepository): MainRepository
}
