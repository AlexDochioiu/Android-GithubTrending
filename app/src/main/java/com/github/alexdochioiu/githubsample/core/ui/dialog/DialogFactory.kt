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
package com.github.alexdochioiu.githubsample.core.ui.dialog

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.alexdochioiu.common.di.ActivityScope
import com.github.alexdochioiu.githubsample.core.utils.ContextMediator
import javax.inject.Inject

@ActivityScope
class DialogFactory<ACTIVITY : AppCompatActivity> @Inject constructor(
    private val localContextMediator: ContextMediator.Local<ACTIVITY>
) {

    fun showDialog(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonResId: Int,
        @StringRes negativeButtonResId: Int,
        positiveButtonAction: ACTIVITY.() -> Unit,
        negativeButtonAction: ACTIVITY.() -> Unit,
    ) {
        localContextMediator.withActivityInstance {
            AlertDialog.Builder(this)
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(positiveButtonResId) { dialog, _ ->
                    dialog.dismiss()
                    positiveButtonAction(this)
                }
                .setNegativeButton(negativeButtonResId) { dialog, _ ->
                    dialog.dismiss()
                    negativeButtonAction(this)
                }
                .show()
        }
    }
}