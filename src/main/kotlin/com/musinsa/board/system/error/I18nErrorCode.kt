package com.musinsa.board.system.error

enum class I18nErrorCode(val code: String) {
    // Common
    UNKNOWN("error.unknown"),
    PERMISSION_DENIED("error.permission-denied"),
    METHOD_ARGUMENT_TYPE_MISMATCH("error.request.type.mismatch"),
    MISSING_REQUEST_PARAMETER("error.request.parameter.missing"),

    // Post
    POST_NOT_FOUND("post.not-found"),

    // Draft
    DRAFT_NOT_FOUND("draft.not-found"),

    // Like Reaction
    POST_LIKE_REACTION_NOT_FOUND("post-like-reaction.not-found"),
    LIKED_POST_NOT_FOUND("post-like-reaction.liked-post.not-found"),
    POST_LIKE_REACTION_DUPLICATE("post-like-reaction.duplicate")
}