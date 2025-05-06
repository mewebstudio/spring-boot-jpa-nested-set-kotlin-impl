package com.mewebstudio.nestedset.repository

import com.mewebstudio.nestedset.entity.Category
import com.mewebstudio.springboot.jpa.nestedset.kotlin.JpaNestedSetRepository

interface CategoryRepository : JpaNestedSetRepository<Category, String> {
    fun existsByName(name: String): Boolean
}
