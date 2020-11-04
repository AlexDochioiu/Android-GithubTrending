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
package com.github.alexdochioiu.githubsample.core

import android.content.Context
import com.github.alexdochioiu.repository.RepositoryComponent.Companion.repositoryComponent
import com.github.alexdochioiu.githubsample.BuildConfig
import com.github.alexdochioiu.githubsample.core.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class ProjApplication : DaggerApplication() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<ProjApplication> {
        return DaggerAppComponent.factory()
            .create(this, repositoryComponent)
    }

    companion object {
        val Context.application: ProjApplication
            get() = applicationContext as ProjApplication
    }
}