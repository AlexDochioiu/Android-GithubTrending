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

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.alexdochioiu.githubsample.R

object ImageViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun ImageView.setImageUrl(url: String?) {
        url?.also {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground) //todo probably should use a spinner
                .into(this)
        }
    }
}