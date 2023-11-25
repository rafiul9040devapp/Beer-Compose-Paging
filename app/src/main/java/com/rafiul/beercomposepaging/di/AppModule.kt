package com.rafiul.beercomposepaging.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.rafiul.beercomposepaging.data.local.BeerDatabase
import com.rafiul.beercomposepaging.data.local.BeerEntity
import com.rafiul.beercomposepaging.data.remote.BeerApi
import com.rafiul.beercomposepaging.data.remote.BeerRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesBeersDatabase(@ApplicationContext context: Context): BeerDatabase {
        return Room.databaseBuilder(context, BeerDatabase::class.java, "beers.db").build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(): BeerApi {
        return Retrofit.Builder().baseUrl(BeerApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(beerDatabase: BeerDatabase,beerApi: BeerApi): Pager<Int,BeerEntity>{
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = BeerRemoteMediator(
                beerDb = beerDatabase,
                beerApi = beerApi
            ),
            pagingSourceFactory = {
                beerDatabase.dao.pagingSource()
            }
        )
    }
}