package com.farmover.server.farmover.payloads.request;

import lombok.Data;

@Data
public class CommentArticleRequest {

    String comment;

    String email;
}
