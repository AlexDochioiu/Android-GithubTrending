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
package com.github.alexdochioiu.githubsample.core.utils

import androidx.appcompat.app.AppCompatActivity
import com.github.alexdochioiu.common.di.ActivityScope
import com.github.alexdochioiu.common.di.ApplicationScope
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.UnicastSubject
import javax.inject.Inject

sealed class ContextMediator {
    private val lockObject = Any()
    private var _subject = UnicastSubject.create<(AppCompatActivity) -> Unit>()
        set(value) {
            synchronized(lockObject) {
                field = value
            }
        }

    val observable: Observable<(AppCompatActivity) -> Unit>
        get() = _subject
            .doOnDispose {
                _subject = UnicastSubject.create()
            }

    fun withActivityInstance(invoker: AppCompatActivity.() -> Unit) {
        synchronized(lockObject) {
            _subject.onNext(invoker)
        }
    }

    @ApplicationScope
    class Global @Inject constructor() : ContextMediator()

    @ActivityScope
    class Local @Inject constructor() : ContextMediator()
}