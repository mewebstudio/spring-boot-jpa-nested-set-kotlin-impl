package com.mewebstudio.nestedset.service

import com.mewebstudio.nestedset.dto.request.CreateCategoryRequest
import com.mewebstudio.nestedset.dto.request.UpdateCategoryRequest
import com.mewebstudio.nestedset.entity.Category
import com.mewebstudio.nestedset.repository.CategoryRepository
import com.mewebstudio.nestedset.exception.BadRequestException
import com.mewebstudio.nestedset.exception.NotFoundException
import com.mewebstudio.nestedset.util.logger
import com.mewebstudio.springboot.jpa.nestedset.kotlin.AbstractNestedSetService
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) : AbstractNestedSetService<Category, String>(categoryRepository) {
    private val log: Logger by logger()

    init {
        log.debug("CategoryService initialized with repository: {}", categoryRepository)
        requireNotNull(categoryRepository) { "CategoryRepository cannot be null" }
    }

    /**
     * Retrieve all categories ordered by left value.
     *
     * @return The list of all categories.
     */
    fun getAllCategories(): List<Category> =
        categoryRepository.findAllOrderedByLeft()

    /**
     * Retrieve a category by ID.
     *
     * @param id The ID of the category.
     * @return The category with the specified ID.
     * @throws NotFoundException if the category is not found.
     */
    fun findById(id: String): Category =
        categoryRepository.findById(id).orElseThrow { NotFoundException("Category not found") }

    /**
     * Rebuild the tree structure of categories.
     *
     * @param category The root category to start rebuilding from.
     */
    @Transactional
    fun rebuildTree(category: Category?) =
        rebuildTree(category, categoryRepository.findAllOrderedByLeft())

    /**
     * Create a new category with the specified name and optional parent.
     *
     * @param request The request containing the category name and optional parent ID.
     * @return The created category.
     * @throws NotFoundException if the parent category is not found.
     */
    @Transactional
    fun create(request: CreateCategoryRequest): Category = run {
        check(!categoryRepository.existsByName(request.name)) {
            "Category with name '${request.name}' already exists"
        }

        createNode(Category(name = request.name, left = 0, right = 0, parent = request.parentId?.let {
            categoryRepository.findById(it).orElseThrow { NotFoundException("Parent not found") }
        })).also { log.info("Created new category: {}", it) }
    }

    /**
     * Update the name and/or parent of a category.
     *
     * @param id The ID of the category to update.
     * @param request The request containing the new name and optional parent ID.
     * @return The updated category.
     * @throws NotFoundException if the category is not found.
     * @throws BadRequestException if the category is not found or if a cyclic reference is detected.
     */
    @Transactional
    fun update(id: String, request: UpdateCategoryRequest): Category = run {
        // Fetch the category to update
        val category = categoryRepository.findById(id).orElseThrow { NotFoundException("Category not found") }
        val parent = if (request.parentId != null) {
            categoryRepository.findById(request.parentId).orElseThrow { NotFoundException("Parent not found") }
        } else {
            null
        }

        // Set the new name
        category.name = request.name

        updateNode(category, parent).also { log.info("Updated: {}", it) }
    }

    /**
     * Delete a category and its subtree.
     *
     * @param id The ID of the category to delete.
     * @throws NotFoundException if the category is not found.
     */
    @Transactional
    fun delete(id: String) =
        deleteNode(categoryRepository.findById(id).orElseThrow {
            NotFoundException("Category not found")
        }).also { log.info("Deleted: {}", id) }

    /**
     * Retrieve the subtree under the specified category.
     *
     * @param id The ID of the category.
     * @return The list of categories in the subtree.
     * @throws NotFoundException if the category is not found.
     */
    fun getSubtree(id: String): List<Category> = run {
        val category = categoryRepository.findById(id).orElseThrow { NotFoundException("Category not found") }
        categoryRepository.findSubtree(category.left, category.right)
    }
}
