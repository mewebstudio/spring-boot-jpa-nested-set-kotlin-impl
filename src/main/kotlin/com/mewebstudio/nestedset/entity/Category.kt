package com.mewebstudio.nestedset.entity

import com.mewebstudio.springboot.jpa.nestedset.kotlin.INestedSetNode
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    name = "categories",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name"], name = "uk_categories_name")
    ]
)
class Category(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "left", nullable = false)
    override var left: Int,

    @Column(name = "right", nullable = false)
    override var right: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_id")
    override var parent: Category? = null
) : AbstractBaseEntity(), INestedSetNode<String, Category> {
    override fun toString(): String =
        "${this::class.simpleName}(id = $id, name = $name, left = $left, right = $right, parent = $parent)"
}
