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

import android.content.Context
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.github.alexdochioiu.common.di.AppContext
import com.github.alexdochioiu.common.di.NetworkingScope
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

@Module(includes = [AppContextModule::class])
internal class NetworkModule {

    @Provides
    @NetworkingScope
    fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message -> Timber.d("okhttp: $message") }
            .apply { level = HttpLoggingInterceptor.Level.BASIC }

    @Provides
    @NetworkingScope
    fun cache(cacheFile: File): Cache = Cache(cacheFile, 10 * 1000 * 1000) //10MB Cache

    @Provides
    @NetworkingScope
    fun cacheFile(@AppContext context: Context): File = File(context.cacheDir, "okhttp_cache")

    @Provides
    @NetworkingScope
    fun okHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        cache: Cache
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build()

    @Provides
    @NetworkingScope
    fun glideDownloader(
        okHttpClient: OkHttpClient
    ): OkHttpUrlLoader.Factory = OkHttpUrlLoader.Factory(okHttpClient)

}