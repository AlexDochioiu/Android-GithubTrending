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
package com.github.alexdochioiu.githubsample.core.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.github.alexdochioiu.githubsample.BR
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<SELF : AppCompatActivity, VIEWMODEL : BaseViewModel<SELF>>(
    private val viewModelClass: Class<VIEWMODEL>,
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<VIEWMODEL>

    @Inject //this is application scoped so we can inject it directly
    lateinit var globalContextMediator: ContextMediator.Global

    lateinit var viewModel: VIEWMODEL
    lateinit var localContextMediator: ContextMediator.Local<SELF> //cannot inject directly because it will fall out of sync during config change

    private var contextMediatorDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("UNCHECKED_CAST") // this is a `hack` to fail fast when [this] is not [SELF]
        val thisAsSelf: SELF = this as SELF

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelClass)

        // Alternatively, I can make [ContextMediator] into a [ViewModel] and use the [ViewModelProvider]
        //  to provide it for the Activity. That would be uglier but it would allow me to keep
        //  localContextMediator as a private property in [BaseViewModel]
        localContextMediator = viewModel.localContextMediator

        val binding: ViewDataBinding = DataBindingUtil.setContentView(this, layoutResId)
        binding.setVariable(BR.viewModel, viewModel)
        binding.lifecycleOwner = this

        viewModel.onRestoreInstanceState(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onResume() {
        super.onResume()
        contextMediatorDisposables.addAll(
            localContextMediator.observable.subscribe {
                @Suppress("UNCHECKED_CAST")
                it.invoke(this as SELF)
            },
            globalContextMediator.observable.subscribe { it.invoke(this) }
        )
    }

    override fun onPause() {
        contextMediatorDisposables.dispose()
        contextMediatorDisposables = CompositeDisposable()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState, isChangingConfigurations)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}