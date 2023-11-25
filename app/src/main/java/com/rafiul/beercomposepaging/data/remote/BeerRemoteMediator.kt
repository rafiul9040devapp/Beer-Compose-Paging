package com.rafiul.beercomposepaging.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rafiul.beercomposepaging.data.local.BeerDatabase
import com.rafiul.beercomposepaging.data.local.BeerEntity
import com.rafiul.beercomposepaging.data.mapper.toBeerEntity
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDb: BeerDatabase,
    private val beerApi: BeerApi
) : RemoteMediator<Int, BeerEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        ((lastItem.id).toInt() / (state.config.pageSize)) + 1
                    }
                }
            }

            delay(2000L)
            val beer = beerApi.getBeers(
                page = loadKey,
                pageCount = state.config.pageSize
            )

            beerDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    beerDb.dao.clearAll()
                }

                val beerEntities = beer.map { it.toBeerEntity() }
                beerDb.dao.upsertAll(beerEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = beer.isEmpty()
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}