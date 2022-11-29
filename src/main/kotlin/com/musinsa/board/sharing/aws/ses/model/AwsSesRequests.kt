package com.musinsa.board.sharing.aws.ses.model

data class AwsSesRequest(
    val recipientEmail: String,
    val title: String,
    val body: String
)