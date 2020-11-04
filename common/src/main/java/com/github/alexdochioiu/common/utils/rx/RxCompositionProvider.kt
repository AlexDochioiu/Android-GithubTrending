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
package com.github.alexdochioiu.common.utils.rx

import android.view.View
import androidx.lifecycle.MutableLiveData
import dagger.Reusable
import io.reactivex.rxjava3.core.SingleTransformer
import javax.inject.Inject

@Reusable
class RxCompositionProvider @Inject constructor() {

    fun <T> handleLoadingVisibility(visibilityLiveData: MutableLiveData<Int>): SingleTransformer<T, T> =
        SingleTransformer<T, T> {
            it
                .doOnSubscribe { visibilityLiveData.postValue(View.VISIBLE) }
                .doAfterTerminate { visibilityLiveData.postValue(View.GONE) }
        }
}