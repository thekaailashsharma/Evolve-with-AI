package com.test.palmapi.di

import com.test.palmapi.repository.ApiService
import com.test.palmapi.repository.ApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                this.serializer = GsonSerializer() {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(client: HttpClient): ApiService {
        return ApiServiceImpl(client = client)
    }
}