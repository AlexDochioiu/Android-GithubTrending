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
import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel(
    protected val disposables: CompositeDisposable,
    val localContextMediator: ContextMediator.Local
) : ViewModel(), LifecycleObserver {

    // I am currently using the save/restore instance state to deduce this. However, I can probably
    //  just keep track of whether it's the first time invoking @OnLifecycleEvent(ON_CREATE)
    private var isConfigChange: Boolean = false

    @OnLifecycleEvent(ON_CREATE)
    private fun onActivityCreated() {
        onActivityCreated(isConfigChange)
        isConfigChange = false
    }

    open fun onActivityCreated(isConfigChange: Boolean) {

    }


    @CallSuper
    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        isConfigChange = savedInstanceState?.getBoolean(KEY_CHANGING_CONFIGURATION, false) ?: false

    }

    @CallSuper
    open fun onSaveInstanceState(outState: Bundle, isChangingConfiguration: Boolean) {
        outState.putBoolean(KEY_CHANGING_CONFIGURATION, isChangingConfiguration)
    }

    @CallSuper
    override fun onCleared() {
        disposables.dispose()
    }

    companion object {
        @VisibleForTesting
        const val KEY_CHANGING_CONFIGURATION: String = "is.changing.config"
    }
}