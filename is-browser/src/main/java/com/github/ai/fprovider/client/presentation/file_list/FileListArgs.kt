package com.github.ai.fprovider.client.presentation.file_list

import android.os.Parcelable
import com.github.ai.fprovider.client.presentation.file_list.model.ProviderData
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileListArgs(
    val providerData: ProviderData? = null
) : Parcelable