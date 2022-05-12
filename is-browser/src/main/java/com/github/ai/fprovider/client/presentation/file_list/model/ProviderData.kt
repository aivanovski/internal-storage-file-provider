package com.github.ai.fprovider.client.presentation.file_list.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProviderData(
    val authority: String,
    val rootPath: String,
    val accessToken: String
) : Parcelable