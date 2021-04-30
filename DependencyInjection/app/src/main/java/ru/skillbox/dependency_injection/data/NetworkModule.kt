package ru.skillbox.dependency_injection.data


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import ru.skillbox.dependency_injection.utils.AppVersionInterceptorQualifier
import ru.skillbox.dependency_injection.utils.LoggingInterceptorQualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun httpClient(@LoggingInterceptorQualifier loggingInterceptor: Interceptor,
                   @AppVersionInterceptorQualifier appVersionInterceptor: Interceptor): OkHttpClient{
        return OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(appVersionInterceptor)
                .followRedirects(true)
                .build()
    }

    @LoggingInterceptorQualifier
    @Provides
    fun getLogingInterceptor(): Interceptor{
        return HttpLoggingInterceptor()
    }

    @AppVersionInterceptorQualifier
    @Provides
    fun getAppVersionInterceptor(): Interceptor{
        return AppVersionInterceptor()
    }

    @Provides
    fun getRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
                .baseUrl("https://google.com")
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun getApi(retrofit: Retrofit): Api{
        return retrofit.create()
    }


}