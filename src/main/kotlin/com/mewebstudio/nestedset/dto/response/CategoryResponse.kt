package com.mewebstudio.nestedset.dto.response

import com.mewebstudio.nestedset.entity.Category
import com.mewebstudio.springboot.jpa.nestedset.kotlin.INestedSetNodeResponse
import java.time.LocalDateTime

data class CategoryResponse(
    override val id: String,

    val name: String,

    override val left: Int,

    override val right: Int,

    val parent: CategoryResponse?,

    override val children: List<CategoryResponse>?,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
) : INestedSetNodeResponse<String>, AbstractBaseResponse() {
    override fun withChildren(children: List<INestedSetNodeResponse<String>>): INestedSetNodeResponse<String> {
        return this.copy(children = children.filterIsInstance<CategoryResponse>())
    }

    companion object {
        fun convert(category: Category, includeParent: Boolean = false): CategoryResponse {
            return CategoryResponse(
                id = category.id,
                name = category.name,
                left = category.left,
                right = category.right,
                parent = if (includeParent && category.parent != null) {
                    convert(category = category.parent!!, includeParent = false)
                } else {
                    null
                },
                children = null,
                createdAt = category.createdAt,
                updatedAt = category.updatedAt
            )
        }
    }
}
