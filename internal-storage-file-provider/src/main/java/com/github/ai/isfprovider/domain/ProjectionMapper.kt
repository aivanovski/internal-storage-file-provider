package com.github.ai.isfprovider.domain

import com.github.ai.isfprovider.entity.Projection

internal class ProjectionMapper {

    fun getProjectionFromColumns(columns: List<String>): List<Projection> {
        val projection = columns
            .mapNotNull { column -> Projection.fromColumnName(column) }

        return if (projection.isNotEmpty()) {
            projection
        } else {
            Projection.values().toList()
        }
    }
}