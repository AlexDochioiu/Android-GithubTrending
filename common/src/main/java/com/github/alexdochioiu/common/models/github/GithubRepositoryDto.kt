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
package com.github.alexdochioiu.common.models.github

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class GithubRepositoryDto(
    @Json(name = "created_at")
    val createdAt: Date,
    @Json(name = "updated_at")
    val updatedAt: Date,
    @Json(name = "archived")
    val archived: Boolean,
    @Json(name = "description")
    val description: String?,
    @Json(name = "forks")
    val forks: Int,
    @Json(name = "forks_count")
    val forksCount: Int,
    @Json(name = "full_name")
    val fullName: String,
    @Json(name = "has_issues")
    val hasIssues: Boolean,
    @Json(name = "html_url")
    val htmlUrl: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "open_issues")
    val openIssues: Int,
    @Json(name = "owner")
    val owner: GithubOwnerDto,
    @Json(name = "stargazers_count")
    val stargazersCount: Int,
    @Json(name = "url")
    val url: String,
    @Json(name = "watchers_count")
    val watchersCount: Int
) : Parcelable