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
package com.github.alexdochioiu.repository.github

import com.github.alexdochioiu.common.di.RepositoryScope
import com.github.alexdochioiu.common.models.github.GithubRepositoryDto
import com.github.alexdochioiu.common.utils.rx.RxSchedulersProvider
import com.github.alexdochioiu.networking.github.GithubService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@RepositoryScope
class GithubRepository @Inject constructor(
    private val githubService: GithubService,
    private val schedulersProvider: RxSchedulersProvider
) {
    // This layer should manage all the data sources (network + database + cache) so that UI doesn't
    //  care where the data is coming from.

    // TODO this should ideally be changed to add pagination instead of just providing the first page
    fun getTrendingRepositories(programmingLanguage: String): Single<List<GithubRepositoryDto>> =
        githubService.getRepositories(PAGE_SIZE, FIRST_PAGE, "$QUERY_KEY_LANGUAGE$programmingLanguage")
            .observeOn(schedulersProvider.computation)
            .map { it.items }

    companion object {
        private const val PAGE_SIZE = 500
        private const val FIRST_PAGE = 1
        private const val QUERY_KEY_LANGUAGE = "language:"
    }
}