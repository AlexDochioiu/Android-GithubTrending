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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * This class is probably not how you expected it. However, I find this to be way better compared to
 * the recommended google approach which would need:
 * * https://github.com/android/architecture-components-samples/blob/main/GithubBrowserSample/app/src/main/java/com/android/example/github/viewmodel/GithubViewModelFactory.kt
 * * https://github.com/android/architecture-components-samples/blob/main/GithubBrowserSample/app/src/main/java/com/android/example/github/di/ViewModelKey.kt
 * * https://github.com/android/architecture-components-samples/blob/main/GithubBrowserSample/app/src/main/java/com/android/example/github/di/ViewModelModule.kt
 *
 * Note: This approach still holds the compile-time safety. If you try to create a ViewModel Factory
 *          for a viewmodel that cannot be injected, dagger will fail to build. It also does not
 *          need a dagger module at all (so less boilerplate).
 */
class ViewModelFactory<VM : ViewModel> @Inject constructor(private val vmProvider: Provider<VM>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = vmProvider.get() as T
}
