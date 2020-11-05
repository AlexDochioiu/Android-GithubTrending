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
package com.github.alexdochioiu.githubsample.ui.github.details

import android.os.Bundle
import com.github.alexdochioiu.common.di.ActivityScope
import com.github.alexdochioiu.common.models.github.GithubRepositoryDto
import com.github.alexdochioiu.githubsample.R
import com.github.alexdochioiu.githubsample.core.ui.BaseViewModel
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import com.github.alexdochioiu.githubsample.core.utils.DateFormatter
import com.github.alexdochioiu.githubsample.core.utils.ResourceProvider
import com.github.alexdochioiu.githubsample.ui.GithubRepoDetailsActivity
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScope
class GithubRepoDetailsViewModel @Inject constructor(
    disposables: CompositeDisposable,
    localContextMediator: ContextMediator.Local,
    repositoryDto: GithubRepositoryDto,
    resourceProvider: ResourceProvider,
    dateFormatter: DateFormatter
) : BaseViewModel(disposables, localContextMediator) {
    val ownerImageUrl = repositoryDto.owner.avatarUrl
    val repositoryName = repositoryDto.name
    val repositoryDescription = repositoryDto.description

    val created: String = resourceProvider.getString(
        R.string.github_repo_details_created,
        dateFormatter.formatDate(repositoryDto.createdAt)
    )

    val lastUpdated = resourceProvider.getString(
        R.string.github_repo_details_last_updated,
        dateFormatter.formatDate(repositoryDto.updatedAt)
    )
    val starsCount = repositoryDto.stargazersCount.toString()
    val forksCount = repositoryDto.forksCount.toString()


    companion object {
        fun prepareBundle(bundle: Bundle, repositoryDto: GithubRepositoryDto) = bundle.apply {
            putParcelable(BUNDLE_KEY_REPOSITORY_DTO, repositoryDto)
        }

        private const val BUNDLE_KEY_REPOSITORY_DTO = "repository:dto"
    }

    @Module
    object DaggerModule {
        @Provides
        @ActivityScope
        fun providesRepositoryDto(activity: GithubRepoDetailsActivity): GithubRepositoryDto =
            activity.intent.extras!!.getParcelable(BUNDLE_KEY_REPOSITORY_DTO)!!
        //force casting to fail fast. cannot recover from this scenario
    }
}