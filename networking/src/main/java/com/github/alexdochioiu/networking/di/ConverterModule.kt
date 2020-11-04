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
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

@Module
internal class ConverterModule {

    @Provides
    @NetworkingScope
    fun moshi(): Moshi = Moshi
        .Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()

    @Provides
    @NetworkingScope
    fun converterFactory(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

}