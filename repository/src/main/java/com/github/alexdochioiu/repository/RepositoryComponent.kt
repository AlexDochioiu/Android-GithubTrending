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
package com.github.alexdochioiu.repository

import android.app.Application
import com.github.alexdochioiu.common.di.RepositoryScope
import com.github.alexdochioiu.networking.NetworkingComponent
import com.github.alexdochioiu.networking.NetworkingComponent.Companion.networkingComponent
import com.github.alexdochioiu.repository.github.GithubRepository
import dagger.BindsInstance
import dagger.Component
import java.util.concurrent.atomic.AtomicReference

@Component(dependencies = [NetworkingComponent::class])
@RepositoryScope
interface RepositoryComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            networkingComponent: NetworkingComponent
        ): RepositoryComponent
    }

    val githubRepository: GithubRepository

    companion object {
        private var repositoryComponentReference: AtomicReference<RepositoryComponent> =
            AtomicReference()

        @get:Synchronized
        val Application.repositoryComponent: RepositoryComponent
            get() {
                val current = repositoryComponentReference.get()

                @Suppress("IfThenToElvis") // normally a nice change but it makes the code harder to read in this instance
                return if (current == null) {
                    DaggerRepositoryComponent.factory().create(this, networkingComponent)
                        .also(repositoryComponentReference::set)
                } else {
                    current
                }
            }
    }
}
