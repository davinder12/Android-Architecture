package com.android.savery.data.network

import androidx.paging.DataSource

open class PagedListNetworkCall<LocalType,RemoteType> (dataSourceFactory: DataSource.Factory<Int, LocalType>, paginationNetworkResource : PageListNetworkResource<LocalType>)
    : PagedListNetworkResource<LocalType, Int>(dataSourceFactory,paginationNetworkResource) {
     constructor(pageList: PaginationList<LocalType, RemoteType>):this(
         DataSourceFactory(
             pageList
         ),pageList)
}