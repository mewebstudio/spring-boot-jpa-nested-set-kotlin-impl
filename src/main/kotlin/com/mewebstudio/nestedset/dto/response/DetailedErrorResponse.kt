package com.mewebstudio.nestedset.dto.response

data class DetailedErrorResponse(
    override val message: String,
    var items: MutableMap<String, String?>
) : ErrorResponse(message)
