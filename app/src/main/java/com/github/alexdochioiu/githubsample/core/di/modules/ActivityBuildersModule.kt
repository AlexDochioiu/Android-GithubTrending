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
package com.github.alexdochioiu.githubsample.core.di.modules

import com.github.alexdochioiu.common.di.ActivityScope
import com.github.alexdochioiu.githubsample.ui.GithubRepoActivity
import com.github.alexdochioiu.githubsample.ui.GithubRepoDetailsActivity
import com.github.alexdochioiu.githubsample.ui.github.details.GithubRepoDetailsViewModel
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): GithubRepoActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [GithubRepoDetailsViewModel.DaggerModule::class])
    abstract fun contributesGithubRepoDetailsActivity(): GithubRepoDetailsActivity

}