package com.mewebstudio.nestedset.entity

import com.mewebstudio.nestedset.entity.generator.GeneratedId
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@MappedSuperclass
abstract class AbstractBaseEntity : Serializable {
    @Id
    @GeneratedId
    @Column(name = "id", nullable = false, updatable = false, length = 26)
    lateinit var id: String

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: LocalDateTime
}
