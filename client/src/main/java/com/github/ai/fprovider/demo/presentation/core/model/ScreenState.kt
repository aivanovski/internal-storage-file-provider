package com.github.ai.fprovider.demo.presentation.core.model

data class ScreenState private constructor(
    val type: ScreenStateType,
    val emptyText: String? = null,
    val errorText: String? = null
) {

    companion object {

        fun data(): ScreenState =
            ScreenState(ScreenStateType.DATA)

        fun loading(): ScreenState =
            ScreenState(ScreenStateType.LOADING)

        fun empty(emptyText: String?): ScreenState =
            ScreenState(
                ScreenStateType.EMPTY,
                emptyText = emptyText
            )

        fun error(errorText: String): ScreenState =
            ScreenState(
                ScreenStateType.ERROR,
                emptyText = null,
                errorText = errorText
            )

        fun dataWithError(errorText: String): ScreenState =
            ScreenState(
                ScreenStateType.DATA_WITH_ERROR,
                emptyText = null,
                errorText = errorText
            )
    }
}