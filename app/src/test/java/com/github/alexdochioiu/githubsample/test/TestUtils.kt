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
package com.github.alexdochioiu.githubsample.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.alexdochioiu.common.utils.rx.RxCompositionProvider
import com.github.alexdochioiu.common.utils.rx.RxSchedulersProvider
import com.github.alexdochioiu.githubsample.core.ui.BaseViewModel
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

@Suppress("UNCHECKED_CAST")
val <T : AppCompatActivity> T.testLocalContextMediator: ContextMediator.Local
    get() = mock {
        on { withActivityInstance(any()) } doAnswer {
            (it.arguments[0] as (activity: T) -> Unit).invoke(this@testLocalContextMediator)
        }
    }

val testSchedulersProvider: RxSchedulersProvider
    get() = mock {
        on { computation } doReturn Schedulers.trampoline()
        on { io } doReturn Schedulers.trampoline()
        on { mainThread } doReturn Schedulers.trampoline()
    }

fun KStubbing<RxCompositionProvider>.setUpHandleLoadingVisibility() {
    on { handleLoadingVisibility<Any>(any()) } doReturn SingleTransformer<Any, Any> {
        it
    }
}

val <T> LiveData<T>.asMutableLiveData
    get() = this as MutableLiveData<T>

val configChangeBundle: Bundle
    get() = mock {
        on { getBoolean(eq(BaseViewModel.KEY_CHANGING_CONFIGURATION), any()) } doReturn true
    }