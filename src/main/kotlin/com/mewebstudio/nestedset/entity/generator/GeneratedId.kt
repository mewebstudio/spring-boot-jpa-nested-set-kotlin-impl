package com.mewebstudio.nestedset.entity.generator

import org.hibernate.annotations.IdGeneratorType

@IdGeneratorType(ULIDGenerator::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class GeneratedId
