package com.harsh.samples.thisweektvshow.infrastructure.di

import android.app.Application
import androidx.room.Room
import com.harsh.samples.thisweektvshow.BuildConfig
import com.harsh.samples.thisweektvshow.data.ConnectivityDataSource
import com.harsh.samples.thisweektvshow.data.InternalConnectivityManager
import com.harsh.samples.thisweektvshow.data.local.TvShowDatabase
import com.harsh.samples.thisweektvshow.data.remote.Constants
import com.harsh.samples.thisweektvshow.data.remote.TheMovieDbApi
import com.harsh.samples.thisweektvshow.data.repository.DefaultTvShowRepository
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository
import com.harsh.samples.thisweektvshow.domain.use_case.GetSearchedShowsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetShowDetailsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetSimilarShowsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetThisWeekTrendingShowsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.ToggleShowFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTvShowDatabase(app: Application): TvShowDatabase {
        return Room.databaseBuilder(
            app,
            TvShowDatabase::class.java,
            TvShowDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit {

        val authInterceptor = Interceptor { chain ->
            val requestWithAuth = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.authToken}")
                .build()
            chain.proceed(requestWithAuth)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.apiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideConnectivityDataSource(app: Application): ConnectivityDataSource {
        return InternalConnectivityManager(app)
    }

    @Provides
    @Singleton
    fun provideTvShowRepository(
        connectivityDataSource: ConnectivityDataSource,
        db: TvShowDatabase,
        retrofit: Retrofit
    ): TvShowRepository {
        return DefaultTvShowRepository(
            connectivityDataSource,
            db.tvShowDao,
            retrofit.create(TheMovieDbApi::class.java)
        )
    }

    @Provides
    @Singleton
    fun provideGetThisWeekTrendingShowsUseCase(repository: TvShowRepository): GetThisWeekTrendingShowsUseCase {
        return GetThisWeekTrendingShowsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetShowDetailsUseCase(repository: TvShowRepository): GetShowDetailsUseCase {
        return GetShowDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSearchedShowsUseCase(repository: TvShowRepository): GetSearchedShowsUseCase {
        return GetSearchedShowsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSimilarShowsUseCase(repository: TvShowRepository): GetSimilarShowsUseCase {
        return GetSimilarShowsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleShowFavoriteUseCase(repository: TvShowRepository): ToggleShowFavoriteUseCase {
        return ToggleShowFavoriteUseCase(repository)
    }
}
