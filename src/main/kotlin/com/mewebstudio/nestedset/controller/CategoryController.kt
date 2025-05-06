package com.mewebstudio.nestedset.controller

import com.mewebstudio.nestedset.dto.request.CreateCategoryRequest
import com.mewebstudio.nestedset.dto.request.UpdateCategoryRequest
import com.mewebstudio.nestedset.dto.response.CategoryResponse
import com.mewebstudio.nestedset.entity.Category
import com.mewebstudio.nestedset.exception.BadRequestException
import com.mewebstudio.nestedset.service.CategoryService
import com.mewebstudio.springboot.jpa.nestedset.kotlin.NestedSetUtil
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {
    @GetMapping("/tree")
    fun tree(): ResponseEntity<List<CategoryResponse>> =
        ResponseEntity.ok(
            NestedSetUtil.tree(
                categoryService.getAllCategories()
            ) { category -> CategoryResponse.convert(category, false) }
        )

    @GetMapping("/ancestors/{id}")
    fun ancestorsById(@PathVariable id: String): ResponseEntity<List<CategoryResponse>> =
        ResponseEntity.ok(
            NestedSetUtil.tree(
                categoryService.getAncestors(categoryService.findById(id))
            ) { category -> CategoryResponse.convert(category, false) }
        )

    @GetMapping("/descendants/{id}")
    fun descendantsById(@PathVariable id: String): ResponseEntity<List<CategoryResponse>> =
        ResponseEntity.ok(
            NestedSetUtil.tree(
                categoryService.getDescendants(categoryService.findById(id))
            ) { category -> CategoryResponse.convert(category, false) }
        )

    @PostMapping
    fun create(@Valid @RequestBody request: CreateCategoryRequest): ResponseEntity<CategoryResponse> =
        ResponseEntity.ok(CategoryResponse.convert(categoryService.create(request), true))

    @GetMapping("/{id}")
    fun show(@PathVariable id: String): ResponseEntity<CategoryResponse> =
        ResponseEntity.ok(CategoryResponse.convert(categoryService.findById(id), true))

    @PostMapping("/{id}/{action}")
    fun move(@PathVariable id: String, @PathVariable action: String): ResponseEntity<CategoryResponse> = run {
        val category = categoryService.findById(id)
        val updatedCategory = when (action) {
            "up" -> categoryService.moveUp(category)
            "down" -> categoryService.moveDown(category)
            else -> throw BadRequestException("Invalid action: $action")
        }
        ResponseEntity.ok(CategoryResponse.convert(updatedCategory, true))
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateCategoryRequest
    ): ResponseEntity<CategoryResponse> =
        ResponseEntity.ok(CategoryResponse.convert(categoryService.update(id, request), true))

    @PatchMapping
    fun rebuild(): ResponseEntity<Void> = run {
        categoryService.rebuildTree(null)
        ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> = run {
        categoryService.delete(id)
        ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}/subtree")
    fun getSubtree(@PathVariable id: String): ResponseEntity<List<Category>> =
        ResponseEntity.ok(categoryService.getSubtree(id))
}
