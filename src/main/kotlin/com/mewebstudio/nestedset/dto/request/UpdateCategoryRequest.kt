package com.mewebstudio.nestedset.dto.request

data class UpdateCategoryRequest(
    val name: String,

    val parentId: String? = null
)
