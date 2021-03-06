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
package com.github.alexdochioiu.githubsample.core.di

import com.github.alexdochioiu.common.di.ApplicationScope
import com.github.alexdochioiu.repository.RepositoryComponent
import com.github.alexdochioiu.githubsample.core.ProjApplication
import com.github.alexdochioiu.githubsample.core.di.modules.ActivityBuildersModule
import com.github.alexdochioiu.githubsample.core.di.modules.AppModule
import com.github.alexdochioiu.githubsample.core.di.modules.RxJavaModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@ApplicationScope
@Component(
    dependencies = [RepositoryComponent::class],
    modules = [
        AppModule::class,
        AndroidInjectionModule::class,
        ActivityBuildersModule::class,
        RxJavaModule::class
    ]
)
abstract class AppComponent : AndroidInjector<ProjApplication> {

    @Component.Factory
    abstract class Factory {
        abstract fun create(
            @BindsInstance appInstance: ProjApplication,
            repositoryComponent: RepositoryComponent
        ): AndroidInjector<ProjApplication>
    }
}