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
package com.github.alexdochioiu.githubsample.ui

import com.github.alexdochioiu.githubsample.R
import com.github.alexdochioiu.githubsample.core.ui.BaseActivity
import com.github.alexdochioiu.githubsample.ui.github.details.GithubRepoDetailsViewModel
import com.github.alexdochioiu.githubsample.ui.github.repo.GithubRepoViewModel

class GithubRepoActivity :
    BaseActivity<GithubRepoViewModel>(
        GithubRepoViewModel::class.java,
        R.layout.activity_github_repo,
    )

class GithubRepoDetailsActivity :
    BaseActivity<GithubRepoDetailsViewModel>(
        GithubRepoDetailsViewModel::class.java,
        R.layout.activity_github_repo_details,
    )