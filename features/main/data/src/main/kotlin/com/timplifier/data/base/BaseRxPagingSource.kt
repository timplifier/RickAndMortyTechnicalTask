package com.timplifier.data.base

import android.net.Uri
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.timplifier.data.utils.DataMapper
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

const val BASE_STARTING_PAGE_INDEX = 0

abstract class BaseRxPagingSource<ValueDto : DataMapper<Value>, Value : Any>(
    private val request: (position: Int) -> Single<BasePagingResponse<ValueDto>>,
) : RxPagingSource<Int, Value>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Value>> {
        val position = params.key ?: BASE_STARTING_PAGE_INDEX

        return request(position).subscribeOn(Schedulers.io())
            .map<LoadResult<Int, Value>?> { response ->
                LoadResult.Page(
                    data = response.results.map { it.toDomain() },
                    prevKey = null,
                    nextKey = when (response.info.next) {
                        null -> null
                        else -> Uri.parse(response.info.next).getQueryParameter("page")?.toInt()
                    }
                )
            }.onErrorReturn {
                LoadResult.Error(it)
            }
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}