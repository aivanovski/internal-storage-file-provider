package com.github.ai.fprovider.utils

import com.github.ai.fprovider.entity.Projection

internal fun List<Projection>.toColumnNames(): List<String> = map { it.columnName }