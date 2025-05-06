package com.mewebstudio.nestedset.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateCategoryRequest(
    @field:NotBlank(message = "Name cannot be blank")
    val name: String,

    val parentId: String? = null
)
