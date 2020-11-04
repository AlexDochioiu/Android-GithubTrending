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

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.alexdochioiu.common.utils.rx.RxCompositionProvider
import com.github.alexdochioiu.common.utils.rx.RxSchedulersProvider
import com.github.alexdochioiu.githubsample.R
import com.github.alexdochioiu.githubsample.core.ui.BaseViewModel
import com.github.alexdochioiu.githubsample.core.ui.dialog.DialogFactory
import com.github.alexdochioiu.githubsample.core.ui.recyclerview.ClickHandler
import com.github.alexdochioiu.githubsample.core.utils.AndroidClassesFactory
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import com.github.alexdochioiu.githubsample.core.utils.exhaustive
import com.github.alexdochioiu.githubsample.ui.GithubRepoDetailsActivity
import com.github.alexdochioiu.githubsample.ui.github.details.GithubRepoDetailsViewModel
import com.github.alexdochioiu.repository.github.GithubRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class GithubRepoViewModel @Inject constructor(
    disposables: CompositeDisposable,
    localContextMediator: ContextMediator.Local,
    private val androidClassesFactory: AndroidClassesFactory,
    private val repository: GithubRepository,
    private val schedulersProvider: RxSchedulersProvider,
    private val compositionProvider: RxCompositionProvider,
    private val rvModelFactory: GithubRepoItem.Factory,
    private val dialogFactory: DialogFactory,
) : BaseViewModel(disposables, localContextMediator) {

    private val _loadingSpinnerVisibility = MutableLiveData(View.GONE)
    val loadingSpinnerVisibility: LiveData<Int> = _loadingSpinnerVisibility

    private val _items = BehaviorSubject.create<List<GithubRepoItem>>()
    val items: Observable<List<GithubRepoItem>> = _items

    val rvClickHandler = ClickHandler<GithubRepoItem> {
        Timber.d("Clicked on $it")

        when (it) {
            is GithubRepoItem.RepoModel -> {
                // I would normally abstract this in a navigator but I won't get into that at this moment
                localContextMediator.withActivityInstance {
                    androidClassesFactory.newIntent.apply {
                        setClass(this@withActivityInstance, GithubRepoDetailsActivity::class.java)
                        putExtras(
                            GithubRepoDetailsViewModel.prepareBundle(
                                androidClassesFactory.newBundle,
                                it.dtoModel
                            )
                        )

                        startActivity(this)
                    }
                }
            }
            is GithubRepoItem.HeaderModel -> {
                //NO-OP
            }
        }.exhaustive
    }

    override fun onActivityCreated(isConfigChange: Boolean) {
        if (!isConfigChange) {
            fetchRepositories()
        }
    }

    private fun fetchRepositories() {
        repository.getTrendingRepositories(LANGUAGE)
            .observeOn(schedulersProvider.mainThread)
            .compose(compositionProvider.handleLoadingVisibility(_loadingSpinnerVisibility))
            .doOnSubscribe { _items.onNext(emptyList()) }
            .subscribe(
                {
                    mutableListOf<GithubRepoItem>()
                        .apply {
                            add(rvModelFactory.makeHeader(TEXT_HEADER))
                            addAll(it.map(rvModelFactory::makeRepoModel))
                        }
                        .also(_items::onNext)
                },
                {
                    dialogFactory.showDialog(
                        R.string.dialog_error_title_generic,
                        R.string.dialog_error_message_generic,
                        R.string.button_retry,
                        R.string.button_exit,
                        {
                            fetchRepositories()
                        },
                        {
                            finishAffinity()
                        }
                    )
                })
            .also(disposables::add)
    }

    companion object {
        @VisibleForTesting
        const val LANGUAGE = "Kotlin"

        @VisibleForTesting
        const val TEXT_HEADER = "Popular $LANGUAGE Repositories"
    }
}