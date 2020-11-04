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
package com.github.alexdochioiu.networking

import android.app.Application
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.github.alexdochioiu.common.di.NetworkingScope
import com.github.alexdochioiu.networking.di.ServiceModule
import com.github.alexdochioiu.networking.github.GithubService
import dagger.BindsInstance
import dagger.Component
import java.util.concurrent.atomic.AtomicReference

@Component(modules = [ServiceModule::class])
@NetworkingScope
interface NetworkingComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): NetworkingComponent
    }

    val githubService: GithubService
    val glideDownloaderFactory: OkHttpUrlLoader.Factory

    companion object {
        private var networkingComponentReference: AtomicReference<NetworkingComponent> =
            AtomicReference()

        @get:Synchronized
        val Application.networkingComponent: NetworkingComponent
            get() {
                val current = networkingComponentReference.get()

                @Suppress("IfThenToElvis") // normally a nice change but it makes the code harder to read in this instance
                return if (current == null) {
                    DaggerNetworkingComponent.factory().create(this)
                        .also(networkingComponentReference::set)
                } else {
                    current
                }
            }
    }
}