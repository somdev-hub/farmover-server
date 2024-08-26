package com.farmover.server.farmover.payloads.request;

import lombok.Data;

@Data
public class UpVoteArticleRequest {

    String email;

    Integer articleId;
}
