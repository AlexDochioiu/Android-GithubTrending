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
package com.github.alexdochioiu.githubsample.ui.github

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.github.alexdochioiu.common.models.github.GithubRepositoryDto
import com.github.alexdochioiu.common.utils.rx.RxCompositionProvider
import com.github.alexdochioiu.githubsample.R
import com.github.alexdochioiu.githubsample.core.ui.dialog.DialogFactory
import com.github.alexdochioiu.githubsample.core.utils.AndroidClassesFactory
import com.github.alexdochioiu.githubsample.core.utils.Navigator
import com.github.alexdochioiu.githubsample.test.*
import com.github.alexdochioiu.githubsample.ui.GithubRepoDetailsActivity
import com.github.alexdochioiu.githubsample.ui.github.repo.GithubRepoItem
import com.github.alexdochioiu.githubsample.ui.github.repo.GithubRepoViewModel
import com.github.alexdochioiu.repository.github.GithubRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test

class GithubRepoViewModelTest {
    //TODO would be good to extract a BaseViewModelTest but I won't do it at the moment

    private val repoDtoMock = mock<GithubRepositoryDto>()

    private val activityMock = mock<AppCompatActivity>()
    private val lifecycleRegistry = LifecycleRegistry(activityMock)

    private val compositeDisposableMock = mock<CompositeDisposable>()
    private val contextMediatorMock = activityMock.testLocalContextMediator
    private val androidClassesFactoryMock = mock<AndroidClassesFactory>()
    private val githubRepositoryMock = mock<GithubRepository>()
    private val compositionProviderMock = mock<RxCompositionProvider> {
        setUpHandleLoadingVisibility()
    }

    private val headerMock = mock<GithubRepoItem.HeaderModel>()
    private val repoModelMock = mock<GithubRepoItem.RepoModel> {
        on { dtoModel } doReturn repoDtoMock
    }
    private val rvModelFactoryMock = mock<GithubRepoItem.Factory> {
        on { makeHeader(any()) } doReturn headerMock
        on { makeRepoModel(any()) } doReturn repoModelMock
    }

    private val dialogFactoryMock = mock<DialogFactory>()
    private val navigatorMock = mock<Navigator>()

    private lateinit var viewModel: GithubRepoViewModel

    @Before
    fun setUp() {
        whenever(activityMock.lifecycle) doReturn lifecycleRegistry

        viewModel = GithubRepoViewModel(
            compositeDisposableMock,
            contextMediatorMock,
            androidClassesFactoryMock,
            githubRepositoryMock,
            testSchedulersProvider,
            compositionProviderMock,
            rvModelFactoryMock,
            dialogFactoryMock,
            navigatorMock
        )

        lifecycleRegistry.addObserver(viewModel)
    }

    @Test
    fun `onCreate - no config change - requests trending repositories and updates items`() {
        whenever(githubRepositoryMock.getTrendingRepositories(any())) doReturn Single.just(
            listOf(
                repoDtoMock
            )
        )
        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        viewModel.items.test()
            .assertValue(listOf(headerMock, repoModelMock))
            .dispose()

        verify(rvModelFactoryMock).makeHeader(GithubRepoViewModel.TEXT_HEADER)
        verify(rvModelFactoryMock).makeRepoModel(same(repoDtoMock))

        verify(compositeDisposableMock).add(any())
    }

    @Test
    fun `onCreate - config changed - no request is made to the repository`() {
        viewModel.onRestoreInstanceState(configChangeBundle)

        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        verifyZeroInteractions(githubRepositoryMock)
        verifyZeroInteractions(compositeDisposableMock)
    }

    @Test
    fun `items - type is BehaviorSubject to support config changes`() {
        viewModel.items assertType BehaviorSubject::class.java
    }

    @Test
    fun `onCreate - attaches loadingVisibility to the request via rx composition`() {
        whenever(githubRepositoryMock.getTrendingRepositories(any())) doReturn Single.just(emptyList())
        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        verify(compositionProviderMock).handleLoadingVisibility<Any>(
            same(viewModel.loadingSpinnerVisibility.asMutableLiveData)
        )
    }

    @Test
    fun `request fails - show error dialog`() {
        val exception = RuntimeException()
        val positiveActionCaptor = argumentCaptor<AppCompatActivity.() -> Unit>()
        val negativeActionCaptor = argumentCaptor<AppCompatActivity.() -> Unit>()

        whenever(githubRepositoryMock.getTrendingRepositories(any())) doReturn Single.error(
            exception
        )
        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        viewModel.items.test().dispose()

        verify(dialogFactoryMock).showDialog(
            eq(R.string.dialog_error_title_generic),
            eq(R.string.dialog_error_message_generic),
            eq(R.string.button_retry),
            eq(R.string.button_exit),
            positiveActionCaptor.capture(),
            negativeActionCaptor.capture()
        )

        //
        whenever(githubRepositoryMock.getTrendingRepositories(any())) doReturn Single.just(emptyList())
        positiveActionCaptor.lastValue.invoke(activityMock)
        viewModel.items.test()
            .assertValue(listOf(headerMock))
            .dispose()
    }

    @Test
    fun `error dialog - positive button - retries`() {
        val exception = RuntimeException()
        val actionCaptor = argumentCaptor<AppCompatActivity.() -> Unit>()

        whenever(githubRepositoryMock.getTrendingRepositories(any())) doReturn Single.error(
            exception
        )
        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        viewModel.items.test().dispose()

        verify(dialogFactoryMock)
            .showDialog(any(), any(), any(), any(), actionCaptor.capture(), any())
        verify(githubRepositoryMock, times(1)).getTrendingRepositories(any())

        actionCaptor.lastValue.invoke(activityMock)
        verify(githubRepositoryMock, times(2)).getTrendingRepositories(any())
    }

    @Test
    fun `error dialog - negative button - finishes affinity`() {
        val exception = RuntimeException()
        val actionCaptor = argumentCaptor<AppCompatActivity.() -> Unit>()

        whenever(githubRepositoryMock.getTrendingRepositories(any())) doReturn Single.error(
            exception
        )
        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        viewModel.items.test().dispose()

        verify(dialogFactoryMock)
            .showDialog(any(), any(), any(), any(), any(), actionCaptor.capture())

        actionCaptor.lastValue.invoke(activityMock)
        verify(activityMock).finishAffinity()
    }

    @Test
    fun `repo item clicked - navigates to repo details screen`() {
        val bundleMock = mock<Bundle>()
        whenever(androidClassesFactoryMock.newBundle) doReturn bundleMock

        viewModel.rvClickHandler.onclick(repoModelMock)

        verify(bundleMock).putParcelable(any(), same(repoDtoMock))
        verify(navigatorMock).navigate(eq(GithubRepoDetailsActivity::class.java), same(bundleMock))
    }
}