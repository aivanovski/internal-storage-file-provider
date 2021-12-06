package com.github.ai.isfprovider.utils

import com.github.ai.isfprovider.entity.Projection

internal fun List<Projection>.toColumnNames(): List<String> = map { it.columnName }