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
package com.github.alexdochioiu.githubsample.ui.github.repo

import androidx.annotation.LayoutRes
import com.github.alexdochioiu.common.models.github.GithubRepositoryDto
import com.github.alexdochioiu.githubsample.R
import com.github.alexdochioiu.githubsample.core.ui.recyclerview.BaseRecyclerViewItem
import javax.inject.Inject

sealed class GithubRepoItem(@LayoutRes layoutResId: Int) : BaseRecyclerViewItem(layoutResId) {

    class HeaderModel(val text: String) : GithubRepoItem(R.layout.item_github_repo_headermodel)

    class RepoModel(
        val dtoModel: GithubRepositoryDto
    ) : GithubRepoItem(R.layout.item_github_repo_repomodel) {
        val shortName get() = dtoModel.name
        val ownerImageUrl get() = dtoModel.owner.avatarUrl
    }

    class Factory @Inject constructor() {
        fun makeHeader(text: String): HeaderModel = HeaderModel(text)

        fun makeRepoModel(model: GithubRepositoryDto) = RepoModel(model)
    }
}