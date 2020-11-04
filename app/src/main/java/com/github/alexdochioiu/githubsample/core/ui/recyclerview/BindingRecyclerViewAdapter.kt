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
package com.github.alexdochioiu.githubsample.core.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Inspired from https://github.com/radzio/android-data-binding-recyclerview/blob/master/recyclerview-binding/src/main/java/net/droidlabs/mvvm/recyclerview/adapter/BindingRecyclerViewAdapter.java
 *
 * This adapter is quite rough, it can use a lot of improvements such as:
 * 1. a Diff strategy
 * 2. a generalised way to save and restore adapter position using activity's save/restore instance
 */
class BindingRecyclerViewAdapter<T : BaseRecyclerViewItem, ACTIVITY : AppCompatActivity>(
    localContextMediator: ContextMediator.Local<ACTIVITY>,
    private val itemsObservable: Observable<List<T>>,
    private val clickHandler: ClickHandler<T> = ClickHandler { }
) : RecyclerView.Adapter<BindingRecyclerViewAdapter<T, ACTIVITY>.ViewHolder>(), LifecycleObserver {

    private val disposables = CompositeDisposable()

    init {
        localContextMediator.withActivityInstance {
            // self-subscribe to the activity's lifecycle
            lifecycle.addObserver(this@BindingRecyclerViewAdapter)

            itemsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items = it }
                .also(disposables::add)

        }
    }

    @OnLifecycleEvent(ON_DESTROY)
    private fun onDestroy() {
        disposables.dispose()
    }

    private var items: List<T> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, layoutId: Int): ViewHolder {
        val context = parent.context

        val binding = LayoutInflater.from(parent.context).run {
            DataBindingUtil.inflate<ViewDataBinding>(this, layoutId, parent, false)
        }
        if (context is LifecycleOwner) binding.lifecycleOwner = context

        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int = items[position].layoutResId

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.model = item
        holder.binding.setVariable(item.variableId, item)
        holder.binding.root.setOnClickListener(holder)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        lateinit var model: T

        override fun onClick(p0: View) {
            clickHandler.onclick(model)
        }

    }
}