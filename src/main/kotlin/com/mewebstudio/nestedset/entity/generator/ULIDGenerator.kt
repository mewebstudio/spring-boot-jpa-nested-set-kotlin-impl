package com.mewebstudio.nestedset.entity.generator

import com.github.f4b6a3.ulid.UlidCreator
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

class ULIDGenerator : IdentifierGenerator {
    /**
     * Generates a ULID (Universally Unique Lexicographically Sortable Identifier) as a string.
     *
     * @param session The session in which the identifier is generated.
     * @param `object` The entity for which the identifier is generated.
     * @return A string representation of the generated ULID.
     */
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): String =
        UlidCreator.getUlid().toString()
}
