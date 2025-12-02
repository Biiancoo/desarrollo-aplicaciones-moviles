package com.example.app_pasteleria_mil_sabores.di

import android.app.Application
import com.example.app_pasteleria_mil_sabores.data.db.AppDatabase
import com.example.app_pasteleria_mil_sabores.data.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        AppDatabase.getDatabase(app)

    @Provides
    @Singleton
    fun provideProductRepository(db: AppDatabase): ProductRepository =
        ProductRepository(db.productDao())
}
