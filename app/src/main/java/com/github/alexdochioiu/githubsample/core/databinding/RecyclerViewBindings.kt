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
package com.github.alexdochioiu.githubsample.core.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.alexdochioiu.githubsample.core.ui.recyclerview.BaseRecyclerViewItem
import com.github.alexdochioiu.githubsample.core.ui.recyclerview.BindingRecyclerViewAdapter
import com.github.alexdochioiu.githubsample.core.ui.recyclerview.ClickHandler
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import io.reactivex.rxjava3.core.Observable

object RecyclerViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["localContextMediator", "items", "clickHandler"])
    fun <T : BaseRecyclerViewItem> RecyclerView.adapter(
        localContextMediator: ContextMediator.Local,
        items: Observable<List<T>>,
        clickHandler: ClickHandler<T>
    ) = BindingRecyclerViewAdapter(localContextMediator, items, clickHandler).also {
        adapter = it
    }
}