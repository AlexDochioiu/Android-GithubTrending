/*
 * Copyright 2020 Alexandru Iustin Dochioiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.alexdochioiu.networking.di

import com.github.alexdochioiu.common.di.NetworkingScope
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

@Module(includes = [NetworkModule::class, ConverterModule::class])
internal class RetrofitModule {

    @Provides
    @NetworkingScope
    fun callAdapterFactory(): CallAdapter.Factory =
        RxJava3CallAdapterFactory.createWithScheduler(
            Schedulers.io()
        )


    @Provides
    @NetworkingScope
    fun retrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .baseUrl(API_BASE_URL)
            .build()


    companion object {
        private const val API_BASE_URL = "https://api.github.com/"
    }
}